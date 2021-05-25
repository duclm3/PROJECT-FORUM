(function($, namespace) {
    'use strict';

    var app = {
        settins: {
            bp: 1024
        },
        dom: {
            $header: $('.js-header')
        },
        crnt: {
            init: function() {
                $(window).on('resize resizecurrent', function() {
                    app.crnt.m = window.innerWidth <= app.settins.bp;
                    app.crnt.d = !app.crnt.m;
                });

                $(window).trigger('resizecurrent');
            }
        },
        methods: {
            responsiveHandlers: function(obj) {
                $(window).on('resize.responsivehandlers responsivehandlers', function() {
                    if(obj.desktopBp === app.crnt.d) return;
                    else obj.desktopBp = app.crnt.d;

                    var $elem = $(obj.elem),
                        ns = obj.namespace ? '.' + obj.namespace : '';

                    if(obj.onDesktop === app.crnt.d) {
                        $.each(obj.events, function(event, func) {
                            $elem.on(event + ns, obj.delegate, func);
                        });
                    } else {
                        $.each(obj.events, function(key) {
                            $elem.unbind(key + ns);
                        });
                    }
                });

                $(window).trigger('responsivehandlers');
            }
        },
        searchVisibleOnMobileBtn: function() {
            var $header = app.dom.$header,
                $btn_open = app.dom.$header.find('.js-header-search-btn-open'),
                $btn_close = app.dom.$header.find('.js-header-search-btn-close');

            app.methods.responsiveHandlers({
                elem: $btn_open,
                namespace: 'searchopen',
                onDesktop: false,
                events: {
                    'click': function() {
                        $header.addClass('header--search');
                    }
                }
            });

            app.methods.responsiveHandlers({
                elem: $btn_close,
                namespace: 'searchclose',
                onDesktop: false,
                events: {
                    'click': function() {
                        $header.removeClass('header--search');
                    }
                }
            });
        },
        dropdowns: function() {

            function toggleList($list, $btn) {
                if(!$list.hasClass('js-dropdown-process')) {
                    $list.addClass('js-dropdown-process');

                    var process = $list.hasClass('dropdown--open') ? 'slideUp' : 'slideDown';

                    $btn = $btn || $list.parents('.js-dropdown').find('[data-dropdown-btn="' + $list.data('dropdown-btn') + '"]');

                    $list.velocity(process, {
                        complete: function() {
                            $list[process === 'slideUp' ? 'removeClass' : 'addClass']('dropdown--open');
                            $list.removeAttr('style').removeClass('js-dropdown-process');
                        }
                    });

                    $btn[process === 'slideUp' ? 'removeClass' : 'addClass']('dropdown__btn--open');
                }
            };

            function closeOpenLists($t) {
                var $list_close = $('[data-dropdown-list].dropdown--open');

                if($t) {
                    var $list_parent = $t.closest('[data-dropdown-list]');

                    $list_close = $list_close.not($list_parent)
                }

                toggleList($list_close);
            };

            $(document).on('click', '.js-dropdown [data-dropdown-btn]', function(e) {
                var $this = $(this),
                    namespace = $this.data('dropdown-btn'),
                    $list = $this.parents('.js-dropdown').find('[data-dropdown-list="' + namespace + '"]');

                closeOpenLists($list);

                toggleList($list, $this);

                e.preventDefault();
                return false;
            });

            $(document).on('click', function(e) {
                var $t = $(e.target);

                closeOpenLists($t);
            });
        },
        init: function() {
            for (var key in this) {
                delete this.init;

                if (typeof this[key] === 'function') {
                    this[key]();
                } else if (this[key].init && typeof this[key].init === 'function') {
                    this[key].init();
                }
            }

            return this;
        }
    };

    window[namespace] = app.init();
})(jQuery, 'app');

function Tags(element) {
    var DOMParent = element;
    var DOMList;
    var DOMInput;
    var dataAttribute;
    var arrayOfList = [];

    function DOMCreate() {
        var ul = document.createElement("ul");
        var input = document.createElement("input");
        DOMParent.appendChild(ul);
        DOMParent.appendChild(input); // first child is <ul>

        DOMList = DOMParent.firstElementChild; // last child is <input>

        DOMInput = DOMParent.lastElementChild;


    }

    function DOMRender() {
        // clear the entire <li> inside <ul>
        DOMList.innerHTML = ""; // render each <li> to <ul>
        if(arrayOfList[0] == "" || arrayOfList[0] == ","){arrayOfList.shift();}

        arrayOfList.forEach(function (currentValue, index) {
            var li = document.createElement("li");
            li.innerHTML = "".concat(currentValue, " <a>&times;</a>");
            li.querySelector("a").addEventListener("click", function () {
                onDelete(index);
            });
            DOMList.appendChild(li);

        });

        setAttribute();
    }

    function onKeyUp() {
        DOMInput.addEventListener("keyup", function (event) {
            var text = this.value.trim(); // check if ',' or 'enter' key was press
            if (text.includes(",")) {
                // check if empty text when ',' is remove
                if (text.replace(",", "") !== "") {
                    // push to array and remove ','
                    arrayOfList.push(text.replace(",", ""));
                } // clear input
                if(arrayOfList[0] == "," || arrayOfList[0] == "") {arrayOfList.shift();}
                document.getElementById("data-input").value = arrayOfList;

                this.value = "";
                DOMRender();
            }

        });
    }

    function onDelete(id) {
        arrayOfList = arrayOfList.filter(function (currentValue, index) {
            if (index === id) {
                return false;
            }

            return currentValue;
        });
        DOMRender();
    }

    function getAttribute() {
        dataAttribute = DOMParent.getAttribute("data-simple-tags");
        dataAttribute = dataAttribute.split(","); // store array of data attribute in arrayOfList

        arrayOfList = dataAttribute.map(function (currentValue) {
            return currentValue.trim();
        });
    }

    function setAttribute() {
        DOMParent.setAttribute("data-simple-tags", arrayOfList.toString());
    }

    getAttribute();
    DOMCreate();
    onKeyUp();
} // run immediately

;

(function () {
    var DOMSimpleTags = document.querySelectorAll(".simple-tags");
    DOMSimpleTags = Array.from(DOMSimpleTags);
    DOMSimpleTags.forEach(function (currentValue) {
        // create Tags
        new Tags(currentValue);
    });
})();
