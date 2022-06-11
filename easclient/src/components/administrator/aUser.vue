<template>
    <div id="hover" v-if="edit" @click="closeEditBox">
        <div class="contanier" @click.stop="()=>{return false}" id="editbox">
            <input class="form-control" hidden :value="editUser.uid">
            <div class="row">
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">姓名</span>
                <input class="form-control" v-model="editUser.name">
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">密码</span>
                <input type="password" class="form-control" v-model="editUser.password">
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">电话</span>
                <input type="tel" class="form-control" v-model="editUser.phone">
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">部门</span>
                <input class="form-control" v-model="editUser.department">
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">权限</span>
                <select class="form-control" v-model="editUser.permission">
                    <option value="PERMISSION_EAS_STUDENT">学生</option>
                    <option value="PERMISSION_EAS_TEACHER">教师</option>
                </select>
            </div>
            <button class="btn btn-primary col mt-3 mb-3" @click="updateUser" href="#">保存</button>
            </div>
        </div>
    </div>
    <button @click="showEditBox({},false)" class="btn btn-outline-primary">录入用户</button>
    <table class="table table-sm" style="text-align: center;">
        <thead class="table-dark">
            <tr>
                <th>学工号</th>
                <th>姓名</th>
                <th>电话</th>
                <th>部门</th>
                <th>权限组</th>
                <th>注册时间</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(item, index) in users" :key="index">
                <td>{{item.uid}}</td>
                <td>{{item.name}}</td>
                <td>{{item.phone}}</td>
                <td>{{item.department}}</td>
                <td>{{permission(item.permission)}}</td>
                <td>{{item.regTime}}</td>
                <td>
                    <a href="#" @click="deleteUser(item.uid)">删除</a>/
                    <a href="#" @click="showEditBox(item,true)">编辑</a>
                </td>
            </tr>
        </tbody>
    </table>
</template>
<script>
import { API, callAPI } from '@/util/api'
import { Permissions } from '@/util/permission'

export default{
    name:'admin-user',
    props:['user'],
    data(){
        return{
            users:[],
            editUser:{uid:-1,register:false},
            edit:false
        }
    },
    mounted(){
        this.loadUsers()
    },
    methods:{
        permission(p){
            switch(p){
                case Permissions.STUDENT:return '学生'
                case Permissions.TEACHER:return '教师'
                case Permissions.ADMINISTRATOR:return '教务'
            }
        },
        loadUsers(){
            callAPI(API.user.userList,{},res=>{
                this.users=res.data
            })
        },
        deleteUser(id){
            if(confirm(`你确定要删除用户${id}吗?`)){
                callAPI(API.user.delete,{uid:id},()=>{
                    this.loadUsers()
                })
            }
        },
        showEditBox(i,e){
            if(e){
                this.editUser=i
                this.editUser.register=false
            }else{
                this.editUser={uid:-1,register:true}
            }
            this.edit=true
        },
        closeEditBox(){
            this.editUser={}
            this.edit=false
        },
        updateUser(){
            callAPI(API.user.updateUser,this.editUser,()=>{
                this.closeEditBox()
                this.loadUsers()
            })
        }
    }
}
</script>