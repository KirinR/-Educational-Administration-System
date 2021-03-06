import axios from "axios"

const API_SERVER='http://127.0.0.1:8080'
const API={
    user:{
        login:{
            url:`${API_SERVER}/api/user/login`,
            method:'POST',
            params:['uid','password','remember']
        },
        loginOut:{
            url:`${API_SERVER}/api/user/loginOut`,
            method:'GET',
            params:[]
        },
        changePwd:{
            url:`${API_SERVER}/api/user/changePwd`,
            method:'POST',
            params:['password']
        },
        updateUser:{
            url:`${API_SERVER}/api/user/updateUser`,
            method:'POST',
            params:['uid','password','name','phone','department','permission','register']
        },
        userList:{
            url:`${API_SERVER}/api/user/userList`,
            method:'GET',
            params:[]
        },
        loginedUser:{
            url:`${API_SERVER}/api/user/loginedUser`,
            method:'GET',
            params:[]
        },
        delete:{
            url:`${API_SERVER}/api/user/delete`,
            method:'POST',
            params:['uid']
        }
    },
    course:{
        getAll:{
            url:`${API_SERVER}/api/course/getAll`,
            method:'GET',
            params:[]
        },
        getTeacherCourses:{
            url:`${API_SERVER}/api/course/getTeacherCourses`,
            method:'GET',
            params:['teacher_id']
        },
        getMyCourses:{
            url:`${API_SERVER}/api/course/getMyCourses`,
            method:'GET',
            params:['']
        },
        get:{
            url:`${API_SERVER}/api/course/get`,
            method:'GET',
            params:['cid']
        },
        delete:{
            url:`${API_SERVER}/api/course/delete`,
            method:'POST',
            params:['cid']
        },
        update:{
            url:`${API_SERVER}/api/course/update`,
            method:'POST',
            params:['cid','name','uid','time','classroom','ctype','session','max','credit','add']
        },
        setSelectable:{
            url:`${API_SERVER}/api/course/setSelectable`,
            method:'POST',
            params:['cid','selectable']
        }
    },
    sc:{
        selectCourse:{
            url:`${API_SERVER}/api/sc/selectCourse`,
            method:'POST',
            params:['student_id','course_id']
        },
        delete:{
            url:`${API_SERVER}/api/sc/delete`,
            method:'POST',
            params:['scid']
        },
        setGrade:{
            url:`${API_SERVER}/api/sc/setGrade`,
            method:'POST',
            params:['scid','usual_grade','final_grade','grade','GPA']
        },
        get:{
            url:`${API_SERVER}/api/sc/get`,
            method:'GET',
            params:['student_id','course_id']
        },
        getMySCs:{
            url:`${API_SERVER}/api/sc/getMySCs`,
            method:'GET',
            params:[]
        },
        getTeacherSCs:{
            url:`${API_SERVER}/api/sc/getTeacherSCs`,
            method:'GET',
            params:[]
        },
        getSelected:{
            url:`${API_SERVER}/api/sc/getSelected`,
            method:'GET',
            params:['cid']
        },
        getIsSelected:{
            url:`${API_SERVER}/api/sc/getIsSelected`,
            method:'GET',
            params:['cid']
        },
        getAll:{
            url:`${API_SERVER}/api/sc/getAll`,
            method:'GET',
            params:[]
        }
    }
}
const callAPI=(api,params,callback)=>{
    switch(api.method){
        case 'GET':{
            let u='?'
            for(let p in params){
                u=u+`${p}=${params[p]}&`
            }
            axios.get(
                api.url+u
            ).then(res=>{
                if(res.data.code==0)callback(res.data)
                else alert(res.data.message)
            }).catch(error=>{alert(error)})
            break
        }
        case 'POST':{
            let u=new FormData()
            for(let p in params){
                u.append(p,params[p])
            }
            axios.post(api.url,u,{headers:{'Content-Type':'multipart/form-data'}}).then(res=>{
                if(res.data.code==0)callback(res.data)
                else alert(res.data.message)
            }).catch(error=>{alert(error)})
            break
        }
        default:break
    }
}
export{
    API,
    callAPI
}