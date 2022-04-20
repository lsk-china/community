import _axios from 'axios'

let axios = _axios.create({
    withCredentials: true,
    baseURL: 'http://localhost:8200/'
})

const auth_test = {
    userinfo: async function () {
        return new Promise((resolved, rejected) => {
            axios.get('/userinfo').then(resp => {
                let body = resp.data
                if (body.code !== 200) {
                    rejected(body.message) 
                } else {
                    resolved(body.data)
                }
            })
        })
    }
}

export default auth_test
