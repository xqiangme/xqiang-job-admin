const project_path_prefix = '/job-admin';
/**
 * Api接口常量
 */
const api = {
    homeView: project_path_prefix + '/index',
    login: project_path_prefix + '/login',
    login_getVerifyCode: project_path_prefix + '/login/getVerifyCode',
    login_in: project_path_prefix + '/login/in',
    login_out: project_path_prefix + '/login/out',

    job: {
        homeCount: project_path_prefix + '/job/getHomeCount',
        listPage: project_path_prefix + '/job/listPage',
        detailHtml: project_path_prefix + '/page/task-detail',
        detail: project_path_prefix + '/job/getJobDetail',
        addHtml: project_path_prefix + '/page/task-add',
        add: project_path_prefix + '/job/save',
        update: project_path_prefix + '/job/update',
        start: project_path_prefix + '/job/start',
        stop: project_path_prefix + '/job/stop',
        delete: project_path_prefix + '/job/delete',
        taskCornHtml: project_path_prefix + '/page/task-corn',
    },
    job_log: {
        listPage: project_path_prefix + '/job-log/listPage',
        detailHtml: project_path_prefix + '/page/task-log-detail',
        detail: project_path_prefix + '/job-log/getLogDetail',
    },
    user: {
        listPage: project_path_prefix + '/user/listPage',
        save: project_path_prefix + '/user/save',
        detail: project_path_prefix + '/user/getUserDetail',
        user_person_html: project_path_prefix + '/page/user-person',
        getPerson: project_path_prefix + '/user/getUserPersonDetail',
        update: project_path_prefix + '/user/update',
        delete: project_path_prefix + '/user/delete',
        update_pwd: project_path_prefix + '/user/update-pwd',
        update_power: project_path_prefix + '/user/update-power',
        add_user_html: project_path_prefix + '/page/user-add',
        update_pwd_html: project_path_prefix + '/page/user-edit-pwd',
        update_power_html: project_path_prefix + '/page/user-edit-power',
        update_detail_html: project_path_prefix + '/page/user-detail'
    },
    comm: {
        selUserView: project_path_prefix + 'common/selUser',
        userMenus: project_path_prefix + 'index/menus',  // 用户目录菜单（左侧）
        userNavMenus: project_path_prefix + 'index/navMenus',  // 用户导航菜单（横向）
        fileUpload: project_path_prefix + 'common/fileUpload'
    },
    gen: {
        tableListData: project_path_prefix + 'generator/list/tableData',
        customGenerateSetting: project_path_prefix + 'generator/setting/',
        customGenerateCode: project_path_prefix + 'generator/custom/generate/code',
        quicklyGenerateCode: project_path_prefix + 'generator/quickly/generate/code',
        quicklyGenerateCodeBatch: project_path_prefix + 'generator/quickly/generate/code/batch',
        checkCodeZipIsExists: project_path_prefix + 'generator/check/codeZip/isExists',
        downloadCodeZip: project_path_prefix + 'generator/download/codeZip'
    }
};

$.ajaxSetup({
    //完成请求后触发。即在success或error触发后触发
    complete: function (xhr, status) {
        const cookieValue = decodeURIComponent(getCookie("COOKIE_USER_INFO"));
        if (xhr.status === 401) {
            layer.msg('未登录,请先登录', {
                offset: '15px', icon: 1, time: 500
            }, function () {
                location.href = api.login;
            });
        }
        if (xhr.status === 405) {
            layer.msg('权限不足', {
                offset: '15px', icon: 1, time: 500
            }, function () {
            });
        } else if (null === cookieValue || "" === cookieValue) {
            location.href = api.login;
        }
    }
});
