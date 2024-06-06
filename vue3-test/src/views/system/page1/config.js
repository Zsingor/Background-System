//表格配置
import {getToolbarConfig} from "@/utils/tableconfig.js";
import {isEmpty} from "@/utils/commons.js";

export function getTableConfig() {
    return {
        formRules: {
            name: [
                {required: true, message: '请输入名称'},
                {min: 1, max: 20, message: '长度在 1 到 20 个字符'}
            ],
            sex: [
                {required: true, message: '请选择性别'},
                {min: 1, max: 10, message: '长度在 1 到 10 个字符'}
            ],
            address: [
                {required: true, message: '请输入地址'},
                {min: 1, max: 50, message: '长度在 1 到 50 个字符'}
            ],
        },
        columns: [
            {type: 'checkbox', width: 50, fixed: 'left'},
            {type: 'seq', width: 50},
            {field: 'name', title: 'name',width:120, sortable: true,},
            {field: 'sex', width:120,title: 'sex'},
            {field: 'createdate', minWidth: 120,title: '创建时间'},
            {
                title: "持续时间段",
                minWidth: 180,
                slots: {default: 'duration'}
            },
            {field: 'address', title: 'Address', minWidth: 120,filters: [
                    {label: '上海', value: '上海'},
                    {label: '北京', value: '北京'},
                    {label: '广州', value: '广州'},
                ]
            },
            {title: '操作', minWidth: 140, fixed: 'right', slots: {default: 'operate'}}
        ],
        queryData:{
            name:"",
            sex:"",
            address:"",
            createdate:"",
            date_list:[],
        },
        toolbarConfig: {
            ...getToolbarConfig(),
            export: true,
        },
        exportConfig: {
            types: ["csv","xlsx","html", "xml", "txt"],
            modes: ["current", "selected", "all"],
            columnFilterMethod: ({column}) => {
                if (column.type === 'checkbox' || column.type === 'seq') {
                    return false
                } else {
                    return !isEmpty(column.property)
                }
            }
        }
    }
}