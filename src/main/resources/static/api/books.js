function getMemberList (params) {
  return $axios({
    url: '/employee/page',
    method: 'get',
    params
  })
}

// 修改---启用禁用接口
function enableOrDisableEmployee (params) {
  return $axios({
    url: '/employee',
    method: 'put',
    data: { ...params }
  })
}

// 新增---添加图书
function addBooks (params) {
  return $axios({
    url: '/books',
    method: 'post',
    data: { ...params }
  })
}

function updatePassword (params) {
  return this.$axios({
    url: '/employee',
    method: 'put',
    data: { ...params }
  })
}

function addUsers (params) {
  return $axios({
    url: '/employee',
    method: 'post',
    data: { ...params }
  })
}

function updateUsers (params) {
  return $axios({
    url: '/employee',
    method: 'put',
    data: { ...params }
  })
}

// 修改---添加图书
function editBooks () {
  return $axios({
    url: `/books`,
    method: 'put'
  })
}

// 修改页面反查详情接口
function queryBookById (id) {
  return $axios({
    url: `/books/${id}`,
    method: 'get'
  })
}

/**
 * 购买图书
 * @returns {*}
 */
function buyBookApi (params){
  return $axios({
    url: `/books/buy`,
    method: 'put',
    data: params
  })
}

