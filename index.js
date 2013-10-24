$(function() {
    var area = $("#input");
    $(".card").click(function () {
        var cursorPos = area.prop('selectionStart');
        var v = area.val(),
            textBefore = v.substring(0, cursorPos),
            textAfter = v.substring(cursorPos, v.length);
        area.val(textBefore + $(this).text() + textAfter);
        area.prop('selectionStart', cursorPos + $(this).text().length);
        area.prop('selectionEnd', cursorPos + $(this).text().length);
    });
});
