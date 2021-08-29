import axios from 'axios'

let authAxios = axios.create({
  withCredentials: true,
  baseURL: 'http://localhost:11001/'
})

const auth = {
  login: async function (identity, password) {
    let resp = null
    try {
      resp = await authAxios.post('/login', {
        identity: identity,
        password: password
      })
      return resp.data
    } catch (e) {
      console.error(e)
    }
  },
  token: function () {
    authAxios.get('/token')
  }
}

export default auth
