$(function() {
    "use strict";
    var cards = [$("#first"), $("#second"), $("#third"), $("#fourth"), $("#fifth"), $("#sixth")], socket, i, area = $("#input"), in_area = [], cardtexts = [], m, s, timer_el = $("#timer");

    function time() {
        s += 1;
        if (s == 60) {
            m += 1;
            s -= 60;
        }
        timer_el.html(m + ":" + (s < 10 ? '0':'') + s);
        setTimeout(function () {
            time();
        }, 1000);
    }
    time(0, -1);

    function verify() {
        var str = area.val(), els = str.split(/(+|\-|\*|\/)/);
    }

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
            $(this).attr('disabled', 'disabled');
        }
        verify();
    });

    area.keyup(function () {
        $("#result").text(eval(area.val()));
    });

    socket = io.connect('http://localhost:1300');
    socket.emit('start');
    socket.on('new', function (data) {
        for (i = 0; i < 6; i += 1) {
            cards[i].text(data[i]);
            cardtexts[i] = parseInt(data[i]);
        }
    });

    $("#submit").click(function () {
        socket.emit('submit', {
            solution: area.val(),
            time: m * 60 + s
        });
    });
});
