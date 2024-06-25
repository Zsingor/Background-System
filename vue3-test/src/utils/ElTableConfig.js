import {parseDate} from "@/utils/commons.js";


//解析日期
export const formatDate=(row,column)=>{
    // 获取单元格数据
    let data = row[column.property]
    return parseDate(data)
}