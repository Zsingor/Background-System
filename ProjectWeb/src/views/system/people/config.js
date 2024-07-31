//表格配置
import {getToolbarConfig} from "@/utils/VxeTableConfig.js";
import {isEmpty} from "@/utils/commons.js";

const checkEmail = (rule, value, callback) => {
    const regEmail = /^([a-zA-Z]|[0-9])(\w|-)+@[a-zA-Z0-9]+\.([a-zA-Z]{2,4})$/;
    if (!regEmail.test(value)&&!isEmpty(value)) {
        callback(new Error('请输入正确的邮箱格式'));
    } else {
        callback();
    }
}

export function getTableConfig() {
    return {
        formRules: {
            name: [
                {required: true, message: '请输入名称'},
                {min: 1, max: 30, message: '长度在 1 到 30 个字符'}
            ],
            password: [
                {required: true, message: '请输入密码'},
                {min: 6, max: 30, message: '长度在 6 到 30 个字符'}
            ],
            email:[
                {required:false,message:"请输入邮箱"},
                {validator: checkEmail, trigger: 'blur'}
            ],
        },
        columns: [
            {type: 'checkbox', width: 50, fixed: 'left'},
            {type: 'seq', width: 50},
            {field: 'id', visible: false, title: 'ID',minWidth:200},
            {field: 'name', title: '用户名',minWidth:120},
            {field: 'roleid', minWidth:120,title: '所属角色',slots: {default: 'role'}},
            {field: 'email', minWidth:120,title: '邮箱'},
            {field: 'description', minWidth:120,title: '账号描述'},
            {field: 'status', minWidth:120,title: '状态',slots: {default: 'status'}},
            {field: 'createTime', minWidth:180,title: '创建时间',formatter: "formatDate"},
            {title: '操作', minWidth: 240, fixed: 'right', slots: {default: 'operate'}}
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
