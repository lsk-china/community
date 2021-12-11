export function createCookieHeader (cookies) {
  let result = ''
  Object.keys(cookies).forEach(ele => {
    result += ele
    result += "="
    result += cookies[ele]
    result += ";"
  })
  return result.substring(0, result.length-1)
}
