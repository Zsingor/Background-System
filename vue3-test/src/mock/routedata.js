// 引用 Mock
import Mock from 'mockjs'

Mock.mock("/route/list", "get", {
    // 属性 list 的值是一个数组，随机生成 1 到 10 个元素
    "data": [
        {
            "name": "system",
            "title": "系统管理",
            "path": "/system",
            "icon": "House",
            "children": [
                {
                    "name": "people",
                    "title": "页面1",
                    "icon": "CircleCheck",
                    "path": "/system/people"
                }
            ]
        },
        {
            "name": "manager",
            "title": "审批管理",
            "path": "/manager",
            "icon": "House",
            "children": [
                {
                    "name": "page2",
                    "title": "页面2",
                    "path": "/manager/routes"
                },
                {
                    "name": "page3",
                    "title": "页面3",
                    "path": "/manager/page3"
                }
            ]
        },
        {
            "name": "people",
            "title": "人员管理",
            "path": "/people",
            "icon": "House",
            "children": []
        }
    ],
    code: 200,
    message: '信息',
});