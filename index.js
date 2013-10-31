$(function() {
    var area = $("#input");
    $(".card").click(function () {
        var cursorPos = area.prop('selectionStart');
        var v = area.val(),
            t = $(this).text();
        if(t != '<-') {
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
});
