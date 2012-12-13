/**
 * User: hsestupin
 * Date: 11/21/12
 * Time: 23:10
 */
$(document).ready(function () {
    $('.container').bind('click', function (e) {
        var el = $(e.target), buttonType = el.attr('tag');

        if (!el.hasClass('btn') || !buttonType) {
            return;
        }

        var item = el.parents('.item'),
            toolbar = item.children('.btn-toolbar'),
            descr = item.children('.description'),
            title = item.children('.title');

        $.ajax({
            type: 'post',
            url: 'reviews',
            data: { tag: buttonType, title: $.trim(title.text()), body: $.trim(descr.text()) },
            success: function (res) {
                var ncontent = '<span class="label">' + buttonType + '</span>';
                toolbar.after(ncontent);
                toolbar.remove();
            }
        });

    });
});
