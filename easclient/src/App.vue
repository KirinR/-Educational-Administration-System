<template>
  <nav class="navbar navbar-dark bg-dark" style="padding-bottom: 0;">
    <div class="container-fluid">
      <a class="navbar-brand" href="#">教务系统</a>
      <SNav v-if="pStudent" :router="router" @setView="setView" v-cloak></SNav>
      <TNav v-if="pTeacher" :router="router" @setView="setView" v-cloak></TNav>
      <ANav v-if="pAdmin" :router="router" @setView="setView" v-cloak></ANav>
    </div>
  </nav>
  <Login v-if="!logined" @login="login" v-cloak></Login>
  <User v-if="logined" :user="user" @loginOut="loginOut" v-cloak></User>
  <Main v-if="logined" :router="router" :user="user" v-cloak></Main>
</template>
<script>
import Login from "./components/Login.vue"
import User from "./components/User.vue"
import { API, callAPI } from "./util/api"
import SNav from "./components/student/sNav.vue"
import TNav from "./components/teacher/tNav.vue"
import ANav from "./components/administrator/aNav.vue"
import { Permissions } from "./util/permission"
import { router } from './util/router'
import Main from './components/Main.vue'
export default {
  name: 'App',
  components: {
    Login,
    User,
    SNav,
    TNav,
    ANav,
    Main
},
  data() {
    return {
      logined: false,
      user: {},
      router:router.administrator.default
    }
  },
  mounted() {
    this.islogin()
  },
  computed: {
    pStudent() {
      return this.user.permission === Permissions.STUDENT
    },
    pTeacher() {
      return this.user.permission === Permissions.TEACHER
    },
    pAdmin(){
      return this.user.permission === Permissions.ADMINISTRATOR
    }
  },
  methods: {
    islogin() {
      callAPI(API.user.loginedUser, {}, res => {
          this.login(res.data)
      })
    },
    login(user) {
      if (user != null) {
        this.user = user
        this.logined = true
        if(this.pStudent){
          this.router=router.student.default
        }else if(this.pTeacher){
          this.router=router.teacher.default
        }else if(this.pAdmin){
          this.router=router.administrator.default
        }
      } else {
        this.user = {}
        this.logined = false
      }
    },
    loginOut() {
      callAPI(API.user.loginOut, {}, () => { this.login(null) }, () => { this.login(null) })
    },
    setView(r){
      if(!this.logined)return;
      switch(this.user.permission){
        case Permissions.STUDENT:{
          let f=false
          for(let i in router.student){
            if(r===router.student[i]){f=true;break;}
          }
          if(!f)return;
          break;
        }
        case Permissions.TEACHER:{
          let f=false
          for(let i in router.teacher){
            if(r===router.teacher[i]){f=true;break;}
          }
          if(!f)return;
          break;
        }
        case Permissions.ADMINISTRATOR:{
          let f=false
          for(let i in router.administrator){
            if(r===router.administrator[i]){f=true;break;}
          }
          if(!f)return;
          break;
        }
      }
      this.router=r
    }
  }
}
</script>
<style>
#hover{
    position: absolute;
    width: 100%;
    height: 100%;
    margin: 0;
    top: 0;
    z-index: 9999;
    background-color: gray;
}
#editbox{
    opacity: 1;
    background-color: white;
}
.input-group-text{
    font-size: 10px;
}
.row{
    width: 100%;
}
</style>