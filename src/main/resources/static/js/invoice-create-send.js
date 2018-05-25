
function selectAllCheckBoxes() {
    var selectedRows = $('table.table tbody input[type=checkbox]');
    console.log('length 'selectedRows.length);
    // console.log($("#check_all"));
    if ($("#check_all").prop("checked")) {

        for (var i = 0; i < selectedRows.length; i++) {
            selectedRows[i].checked = true;
        }
        calculateTotalSum();
    } else {
        for (var i = 0; i < selectedRows.length; i++) {
            selectedRows[i].checked = false;
        }
        calculateTotalSum();
    }
}



