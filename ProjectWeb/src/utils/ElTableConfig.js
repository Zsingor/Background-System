import {isEmpty, parseDate} from "@/utils/commons.js";
import * as XLSX from "xlsx";


/**
 * 解析日期
 * @param {*} row 
 * @param {*} column 
 * @returns 正确的日期格式
 */
export const formatDate=(row,column)=>{
    // 获取单元格数据
    let data = row[column.property]
    return parseDate(data)
}

/**
 * 导出xlsx文件
 * @param {Object} exportForm 导出设置 {fileName, data, filter}
 * @param {Object} tableRef 表格ref
 * @param {Array} filterList 过滤列表
 * @param {Array} tableData 表格的全部数据
 * @param {Function} callback 处理数据的回调函数
 */
export const exportTableData = (exportForm, tableRef, filterList, tableData, callback) => {
    const { data, fileName, filter } = exportForm;
    const isSelectedRows = data === 1;
    let list = isSelectedRows ? tableRef.value.getSelectionRows() : tableData;

    list = JSON.parse(JSON.stringify(list));
    if (callback) {
        list = callback(list);
    }

    const exportData = formatJson(filter, list);
    const filename = fileName ? `${fileName}.xlsx` : `表格-${Date.now()}.xlsx`;

    const headerZh = handleExportHeader(filterList, filter);
    const arrayWithHeader = [headerZh, ...exportData];
    const ws = XLSX.utils.json_to_sheet(arrayWithHeader, {
        header: filter,
        skipHeader: true,
    });
    const wb = XLSX.utils.book_new();
    XLSX.utils.book_append_sheet(wb, ws, 'Sheet1');
    XLSX.writeFile(wb, filename);
};

/**
 * 处理数据使之只导出固定列
 * @param {Array} filterVal 过滤的字段列表
 * @param {Array} data 数据数组
 * @returns {Array} 处理后的数据
 */
const formatJson = (filterVal, data) => {
    return data.map(item => {
        return Object.fromEntries(
            Object.entries(item).filter(([key]) => filterVal.includes(key))
        );
    });
};

/**
 * 处理导出的数据头
 * @param {Array} filterList 过滤列表
 * @param {Array} filter 导出的字段列表
 * @returns {Object} 导出的数据头
 */
const handleExportHeader = (filterList, filter) => {
    const tempMap = filterList.reduce((map, item) => {
        map[item.value] = item.title;
        return map;
    }, {});

    return filter.reduce((headerZh, value) => {
        if (tempMap[value]) {
            headerZh[value] = tempMap[value];
        }
        return headerZh;
    }, {});
};