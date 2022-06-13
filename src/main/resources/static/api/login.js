function loginApi(data) {
  return $axios({
    'url': '/employee/login000',
    'method': 'post',
    data
  })
}

function logoutApi(){
  return $axios({
    'url': '/employee/logout',
    'method': 'post',
  })
}
