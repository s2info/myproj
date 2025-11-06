function tableRowSpanning(Table, spanning_row_index) {
    var RowspanTd = false;
    var RowspanText = false;
    var RowspanCount = 0;
    var Rows = $('tbody tr', Table);
    var RowspanTd2 = false;
    var RowspanText2 = false;
    var RowspanCount2 = 0;
    var Rows2 = $('tbody tr tr', Table);

    $.each(Rows, function () {
        var This = $('td', this)[spanning_row_index];
        var text = $(This).text();

        if (RowspanTd == false) {
            RowspanTd = This;
            RowspanText = text;
            RowspanCount = 1;
        }
        else if (RowspanText != text) {
            $(RowspanTd)
            .attr('rowSpan', RowspanCount);

            RowspanTd = This;
            RowspanText = text;
            RowspanCount = 1;
        }
        else {
            $(This)
            .remove();
            RowspanCount++;
        }
    });
    $(RowspanTd)
        .attr('rowSpan', RowspanCount);
}