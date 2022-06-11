<template>
    <div id="hover" v-if="edit" @click="closeEditBox">
    <div class="contanier" @click.stop="()=>{return false}" id="editbox">
        <div class="contanier" style="text-align: center;font-weight: 600;">修改密码</div>
        <input class="form-control" hidden :value="user.uid">
        <div class="row">
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">密码</span>
                <input class="form-control" type="password" v-model="password">
            </div>
            <button class="btn btn-primary col mt-3 mb-3" @click="changePwd" href="#">保存</button>
        </div>
    </div>
    </div>
    <div style="text-align: center;">
        <svg @click="showEditBox" xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor" class="bi bi-person-circle"
            viewBox="0 0 16 16">
            <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z" />
            <path fill-rule="evenodd"
                d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z" />
        </svg><br>
        <span>
            {{ user.name }}
            <svg style="cursor:pointer" @click="this.$emit('loginOut')" xmlns="http://www.w3.org/2000/svg" width="18" height="18" fill="currentColor"
                class="bi bi-box-arrow-in-right" viewBox="0 0 16 16">
                <path fill-rule="evenodd"
                    d="M6 3.5a.5.5 0 0 1 .5-.5h8a.5.5 0 0 1 .5.5v9a.5.5 0 0 1-.5.5h-8a.5.5 0 0 1-.5-.5v-2a.5.5 0 0 0-1 0v2A1.5 1.5 0 0 0 6.5 14h8a1.5 1.5 0 0 0 1.5-1.5v-9A1.5 1.5 0 0 0 14.5 2h-8A1.5 1.5 0 0 0 5 3.5v2a.5.5 0 0 0 1 0v-2z" />
                <path fill-rule="evenodd"
                    d="M11.854 8.354a.5.5 0 0 0 0-.708l-3-3a.5.5 0 1 0-.708.708L10.293 7.5H1.5a.5.5 0 0 0 0 1h8.793l-2.147 2.146a.5.5 0 0 0 .708.708l3-3z" />
            </svg>
        </span>
    </div>
</template>
<script>
import { API, callAPI } from '@/util/api'

export default{
    name: 'user-component',
    props:['user'],
    data(){
        return {
            password:'',
            edit:false
        }
    },
    methods:{
        showEditBox(){
            this.edit=true
        },
        closeEditBox(){
            this.edit=false
        },
        changePwd(){
            callAPI(API.user.changePwd,{password:this.password},()=>{
                location.reload()
            })
        }
    }
}
</script>