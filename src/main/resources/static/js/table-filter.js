$(document).ready(function(){
    $('.filterable .btn-filter').click(function(){
        var $panel = $(this).parents('.filterable'),
            $filters = $panel.find('.filters input'),
            $selectors = $panel.find('.filters select'),
            $tbody = $panel.find('.table tbody');
        if ($filters.prop('disabled') == true || $selectors.prop('disabled') == true) {
            $filters.prop('disabled', false);
            $selectors.prop('disabled', false);
            $filters.first().focus();
        } else {
            $(".filter_body").append($rows);
            $selectors.val('Roles').prop('disabled', true);
            $(".sel_status").val('Status').prop('disabled', true);
            $filters.val('').prop('disabled', true);
            $tbody.find('.no-result').remove();
            $tbody.find('tr').show();
        }
    });

    var $rows = $(".filter_body tr");
    $(".sel_roles").change(function () {
        $(".filter_body").append($rows);
        $(".sel_roles option:selected").each(function () {
            if ($(this).val()=="Roles"){
                $(".filter_body").append($rows);
            } else {
                var $choice = $(this).val().toUpperCase();
                var $fl =  $(".filter_body td[class='filter_role']").filter(function () {
                    return $(this).text().indexOf($choice) === -1;
                }).parent().remove();
            }
        });
    });
    $(".sel_status").change(function () {
        $(".filter_body").append($rows);
        $(".sel_status option:selected").each(function () {
            if ($(this).val()=="Status"){
                $(".filter_body").append($rows);
            } else if ($(this).text() == "ACTIVE") {
                var $fl =  $(".filter_body td[class='filter_status']").filter(function () {
                    return $(this).text().indexOf("INACTIVE") !== -1;
                }).parent().remove();
            } else {
                var $choice = $(this).val().toUpperCase();
                var $fl =  $(".filter_body td[class='filter_status']").filter(function () {
                    return $(this).text().indexOf($choice) === -1;
                }).parent().remove();
            }
        });
    });

    $('.filterable .filters input').keyup(function(e){
        /* Ignore tab key */
        var code = e.keyCode || e.which;
        if (code == '9') return;
        /* Useful DOM data and selectors */
        var $input = $(this),
            inputContent = $input.val().toLowerCase(),
            $panel = $input.parents('.filterable'),
            column = $panel.find('.filters th').index($input.parents('th')),
            $table = $panel.find('.table'),
            $rows = $table.find('tbody tr');
        /* Dirtiest filter function ever ;) */
        var $filteredRows = $rows.filter(function(){
            var value = $(this).find('td').eq(column).text().toLowerCase();
            return value.indexOf(inputContent) === -1;
        });
        /* Clean previous no-result if exist */
        $table.find('tbody .no-result').remove();
        /* Show all rows, hide filtered ones (never do that outside of a demo ! xD) */
        $rows.show();
        $filteredRows.hide();
        /* Prepend no-result row if all rows are filtered */
        if ($filteredRows.length === $rows.length) {
            $table.find('tbody').prepend($('<tr class="no-result text-center"><td colspan="'+ $table.find('.filters th').length +'">No result found</td></tr>'));
        }
    });
});