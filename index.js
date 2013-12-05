$(function() {
    "use strict";
    var cards = [$("#first"), $("#second"), $("#third"), $("#fourth"), $("#fifth"), $("#sixth")], socket, i, area = $("#input"), in_area = [], cardtexts = [];

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
            in_area.push(t);
        }
    });

    area.keyup(function () {
        $("#result").text(eval(area.val()));
    });

    $(".card").mousedown(function () {
        if ($(this).text() === '\u232b') {
            return;
        }
        $(this).prop('selectionStart', 0);
        $(this).prop('selectionEnd', $(this).text().length);
    });

    socket = io.connect('http://localhost:1300');
    socket.emit('start');
    socket.on('new', function (data) {
        for (i = 0; i < 6; i += 1) {
            cards[i].text(data[i]);
            cardtexts[i] = data[i];
        }
    });

    $("#submit").click(function () {
        socket.emit('submit', area.val());
    });
});
