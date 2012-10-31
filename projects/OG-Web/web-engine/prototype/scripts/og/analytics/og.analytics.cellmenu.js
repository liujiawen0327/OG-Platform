/*
 * Copyright 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 * Please see distribution for license.
 */
$.register_module({
    name: 'og.analytics.CellMenu',
    dependencies: ['og.common.gadgets.mapping'],
    obj: function () {
        var module = this, 
            icons = '.og-num, .og-icon-new-window-2', 
            open_icon = '.og-small', 
            open_inplace = '.og-icon-down-chevron', 
            expand_class = 'og-expanded', 
            panels = ['south', 'dock-north', 'dock-center', 'dock-south'], 
            width = 34,
            mapping = og.common.gadgets.mapping, 
            type_map = mapping.data_type_map,
            onlydepgraphs = Object.keys(type_map) // a list of datatypes that only support depgraph gadgets
                .filter(function (key) {return type_map[key].length === 1 && type_map[key][0] === 0});
        var constructor = function (grid) {
            var cellmenu = this, timer, depgraph = !!grid.config.source.depgraph, parent = grid.elements.parent,
                inplace_config; cellmenu.grid = grid;
            if (og.analytics.containers.initialize) throw new Error(module.name + ': there are no panels');
            og.api.text({module: 'og.analytics.cell_options'}).pipe(function (template) {
                (cellmenu.menu = $(template)).hide()
                .on('mouseleave', function () {
                    clearTimeout(timer), cellmenu.menu.removeClass(expand_class), cellmenu.hide();
                })
                .on('mouseenter', open_icon, function () {
                    clearTimeout(timer), timer = setTimeout(function () {cellmenu.menu.addClass(expand_class);}, 500);
                })
                .on('mouseenter', function () {
                    $.data(cellmenu, 'hover', true);
                })
                .on('click', open_icon, function () {
                    cellmenu.menu.addClass(expand_class);
                })
                .on('mouseenter', icons, function () {
                    var panel = panels[$(this).text() - 1];
                    panels.forEach(function (val) {og.analytics.containers[val].highlight(true, val === panel);});
                })
                .on('mouseleave', icons, function () {
                    panels.forEach(function (val) {og.analytics.containers[val].highlight(false);});
                })
                .on('click', icons, function () {
                    var panel = panels[+$(this).text() - 1], cell = cellmenu.current,
                        options = mapping.options(cell, grid, panel);
                    cellmenu.hide();
                    if (!panel) og.analytics.url.launch(options); else og.analytics.url.add(panel, options);
                });
                grid.on('cellhoverin', function (cell) {
                    cellmenu.menu.removeClass(expand_class);
                    clearTimeout(timer);
                    var type = cell.type, hide = !(cellmenu.current = cell).value
                        || (cell.col < (depgraph ? 1 : 2)) || (cell.right > parent.width())
                        || (depgraph && $.inArray(type, onlydepgraphs) > -1);
                    if (hide) cellmenu.hide(); else cellmenu.show();
                }).on('cellhoverout', function () {
                    clearTimeout(timer);
                    setTimeout(function () {if (!cellmenu.menu.is(':hover')) {cellmenu.hide();}});
                });

                og.api.text({module: 'og.analytics.inplace_tash'}).pipe(function (tmpl_inplace) {
                    $.when(
                        inplace_config = ({$cntr:  $('.og-inplace', cellmenu.menu), tmpl: tmpl_inplace}))
                    .then(function(){
                        cellmenu.inplace = new og.common.util.ui.DropMenu(inplace_config);
                        container = new og.common.gadgets.GadgetsContainer('.OG-analytics-inplace-', 'container');
                        container.init();
                        cellmenu.inplace.$dom.toggle.on('click', function() {
                            cellmenu.inplace.toggle();
                            cellmenu.create_inplace();
                        });

                    });

                });

            });
        };
        constructor.prototype.create_inplace = function () {
            var cellmenu = this, cell = cellmenu.current, panel = 'inplace', options;
            options = mapping.options(cell, cellmenu.grid, panel);
            container.add([options]);
            //freeze this menu
            //create new menu for grid
            //add new gadget to container
        };
        constructor.prototype.hide = function () {
          /* var cellmenu = this;
            if (cellmenu.menu && cellmenu.menu.length) {
                cellmenu.menu.hide();
                cellmenu.inplace.close();
                $.data(cellmenu, 'hover', false);
            }*/
        };
        constructor.prototype.show = function () {
            var cellmenu = this, current = this.current;
            if (cellmenu.menu && cellmenu.menu.length){
                (cellmenu.menu).appendTo($('body')).css({top: current.top, left: current.right - width}).show();
            }
        };
        return constructor;
    }
});
