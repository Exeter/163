$(function() {
    "use strict";
    function time(m, s) {
        s += 1;
        if (s == 60) {
            m += 1;
            s -= 60;
        }
        $("#timer").html(m + ":" + (s < 10 ? '0':'') + s);
        setTimeout(function () {
            time(m, s);
        }, 1000);
    }
    time(0, -1);

    var area = $("#input");
    $(".card").click(function () {
        var cursorPos = area.prop('selectionStart');
        var v = area.val(),
            t = $(this).text();
        if(t != '\u232b') {
            var textBefore = v.substring(0, cursorPos),
                textAfter = v.substring(cursorPos, v.length);
            area.val(textBefore + t + textAfter);
            area.prop('selectionStart', cursorPos + t.length);
            area.prop('selectionEnd', cursorPos + t.length);
        } else {
            var textBefore = v.substring(0, cursorPos - 1),
                textAfter = v.substring(cursorPos, v.length);
            area.val(textBefore + textAfter);
            area.prop('selectionStart', cursorPos - 1);
            area.prop('selectionEnd', cursorPos - 1);
        }
    });

    area.keypress(function (event) {
        if (e && e.keyCode === 13) {
            $("#result").text(evaluate(area.val()));
        }
    });
});
