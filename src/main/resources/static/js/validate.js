
function isValidUsername (str) {
  return ['admin', 'editor'].indexOf(str.trim()) >= 0;
}

function isExternal (path) {
  return /^(https?:|mailto:|tel:)/.test(path);
}

function isCellPhone (val) {
  if (!/^1(3|4|5|6|7|8)\d{9}$/.test(val)) {
    return false
  } else {
    return true
  }
}

//校验账号
function checkBookName (rule, value, callback){
  console.log(value)
  if (value == "") {
    callback(new Error("请输入图书名称"))
  }else if (value.length > 25) {
    callback(new Error("图书名称长度应是1-25"))
  }else {
    callback()
  }
}

function checkUserName (rule, value, callback){
  console.log(value)
  if (value == "") {
    callback(new Error("请输入用户名称名称"))
  }else if (value.length > 15) {
    callback(new Error("图书名称长度应是1-15"))
  }else {
    callback()
  }
}

function checkPassword (rule, value, callback){
  console.log(value)
  if (value == "") {
    callback(new Error("请输入密码"))
  }else if (value.length > 20||value.length<6) {
    callback(new Error("密码长度应是6-20"))
  }else {
    callback()
  }
}

//校验姓名
function checkBookType (rule, value, callback){
  if (value == "") {
    callback(new Error("请输入图书类型"))
  } else if (value.length > 10) {
    callback(new Error("图书类型长度应是1-10"))
  } else {
    callback()
  }
}

function checkPrice (rule, value, callback){
  // let phoneReg = /(^1[3|4|5|6|7|8|9]\d{9}$)|(^09\d{8}$)/;
  if (value == "") {
    callback(new Error("请输入图书价格"))
  }else if(value>1000){
    callback(new Error("图书价格要少于1000"))
  } else {
    callback()
  }
}

function checkCount (rule, value, callback){
  // let phoneReg = /(^1[3|4|5|6|7|8|9]\d{9}$)|(^09\d{8}$)/;
  if (value == "") {
    callback(new Error("请输入图书库存数量"))
  } else if(value>100){
    callback(new Error("图书库存要少于100本"))
  }else{
    callback()
  }
}


function validID (rule,value,callback) {
  // 身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X
  let reg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/
  if(value == '') {
    callback(new Error('请输入身份证号码'))
  } else if (reg.test(value)) {
    callback()
  } else {
    callback(new Error('身份证号码不正确'))
  }
}