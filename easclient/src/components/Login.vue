<template>
    <div class="container" style="width: 460px;">
        <h1 style="text-align: center;" class="mt-4">
            <svg xmlns="http://www.w3.org/2000/svg" width="64" height="64" fill="currentColor"
                class="bi bi-box-arrow-in-left" viewBox="0 0 16 16">
                <path fill-rule="evenodd"
                    d="M10 3.5a.5.5 0 0 0-.5-.5h-8a.5.5 0 0 0-.5.5v9a.5.5 0 0 0 .5.5h8a.5.5 0 0 0 .5-.5v-2a.5.5 0 0 1 1 0v2A1.5 1.5 0 0 1 9.5 14h-8A1.5 1.5 0 0 1 0 12.5v-9A1.5 1.5 0 0 1 1.5 2h8A1.5 1.5 0 0 1 11 3.5v2a.5.5 0 0 1-1 0v-2z" />
                <path fill-rule="evenodd"
                    d="M4.146 8.354a.5.5 0 0 1 0-.708l3-3a.5.5 0 1 1 .708.708L5.707 7.5H14.5a.5.5 0 0 1 0 1H5.707l2.147 2.146a.5.5 0 0 1-.708.708l-3-3z" />
            </svg>
        </h1>
        <div class="input-group mb-2 mt-2">
            <span class="input-group-text">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-person"
                    viewBox="0 0 16 16">
                    <path
                        d="M8 8a3 3 0 1 0 0-6 3 3 0 0 0 0 6zm2-3a2 2 0 1 1-4 0 2 2 0 0 1 4 0zm4 8c0 1-1 1-1 1H3s-1 0-1-1 1-4 6-4 6 3 6 4zm-1-.004c-.001-.246-.154-.986-.832-1.664C11.516 10.68 10.289 10 8 10c-2.29 0-3.516.68-4.168 1.332-.678.678-.83 1.418-.832 1.664h10z" />
                </svg>
            </span>
            <input class="form-control" placeholder="用户名" v-model="params.uid">
        </div>
        <div class="input-group mb-2 mt-2">
            <span class="input-group-text">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" class="bi bi-lock"
                    viewBox="0 0 16 16">
                    <path
                        d="M8 1a2 2 0 0 1 2 2v4H6V3a2 2 0 0 1 2-2zm3 6V3a3 3 0 0 0-6 0v4a2 2 0 0 0-2 2v5a2 2 0 0 0 2 2h6a2 2 0 0 0 2-2V9a2 2 0 0 0-2-2zM5 8h6a1 1 0 0 1 1 1v5a1 1 0 0 1-1 1H5a1 1 0 0 1-1-1V9a1 1 0 0 1 1-1z" />
                </svg>
            </span>
            <input class="form-control" placeholder="密码" type="password" v-model="params.password">
        </div>
        <div class="row mb-2 mt-2">
        <div class="col input-group">
            <span class="input-group-text">
                <svg xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor"
                    class="bi bi-patch-check" viewBox="0 0 16 16">
                    <path fill-rule="evenodd"
                        d="M10.354 6.146a.5.5 0 0 1 0 .708l-3 3a.5.5 0 0 1-.708 0l-1.5-1.5a.5.5 0 1 1 .708-.708L7 8.793l2.646-2.647a.5.5 0 0 1 .708 0z" />
                    <path
                        d="m10.273 2.513-.921-.944.715-.698.622.637.89-.011a2.89 2.89 0 0 1 2.924 2.924l-.01.89.636.622a2.89 2.89 0 0 1 0 4.134l-.637.622.011.89a2.89 2.89 0 0 1-2.924 2.924l-.89-.01-.622.636a2.89 2.89 0 0 1-4.134 0l-.622-.637-.89.011a2.89 2.89 0 0 1-2.924-2.924l.01-.89-.636-.622a2.89 2.89 0 0 1 0-4.134l.637-.622-.011-.89a2.89 2.89 0 0 1 2.924-2.924l.89.01.622-.636a2.89 2.89 0 0 1 4.134 0l-.715.698a1.89 1.89 0 0 0-2.704 0l-.92.944-1.32-.016a1.89 1.89 0 0 0-1.911 1.912l.016 1.318-.944.921a1.89 1.89 0 0 0 0 2.704l.944.92-.016 1.32a1.89 1.89 0 0 0 1.912 1.911l1.318-.016.921.944a1.89 1.89 0 0 0 2.704 0l.92-.944 1.32.016a1.89 1.89 0 0 0 1.911-1.912l-.016-1.318.944-.921a1.89 1.89 0 0 0 0-2.704l-.944-.92.016-1.32a1.89 1.89 0 0 0-1.912-1.911l-1.318.016z" />
                </svg>
            </span>
            <input class="form-control" v-model="params.vcode" placeholder="验证码">
        </div>
        <canvas class="col" id="verify-code" @click="nextVCode()"></canvas>
        </div>
        <div class="form-check mb-2 mt-2">
            <input class="form-check-input" type="checkbox" v-model="params.remember">
            <span class="form-check-label">记住我</span>
        </div>
        <button class="btn btn-primary" @click="tryLogin">登录</button>
    </div>
</template>
<script>
import {callAPI,API} from '../util/api'
const crypto=require('crypto-js')
export default {
    name: 'login-component',
    data(){
        return{
            params:{remember:false},
            vcode:'',
            vcodeCtx:{}
        }
    },
    mounted(){
        this.vcodeCtx=document.getElementById('verify-code').getContext('2d')
        this.nextVCode()
    },
    methods:{
        tryLogin(){
            if(this.vcode!=this.params.vcode){
                alert('验证码错误')
                this.params={remember:this.params.remember}
                this.nextVCode();
                return
            }
            this.params.password=crypto.MD5(this.params.password)
            callAPI(API.user.login,this.params,res=>{
                if(res.code!=0){
                    this.params={remember:this.params.remember}
                    this.nextVCode();
                    return
                }
                this.$emit('login',res.data)
                this.params={remember:false}
            })
        },
        nextVCode(){
            if(this.vcodeCtx==null)this.vcodeCtx=document.getElementById('verify-code').getContext('2d')
            this.vcode=Math.random().toString(36).slice(2).substring(0,4)
            let w=this.vcodeCtx.canvas.width,h=this.vcodeCtx.canvas.height
            this.vcodeCtx.clearRect(0,0,w,h)
            this.vcodeCtx.font=`${h*0.6}px bold`
            let mw=this.vcodeCtx.measureText(this.vcode).width
            this.vcodeCtx.fillText(this.vcode,(w-mw)/2,h*0.7)
        }
    }
}
</script>
<style>
#verify-code{
    height: 37.9px;
}
</style>
