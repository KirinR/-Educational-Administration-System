<template>
    <div id="hover" v-if="edit" @click="closeEditBox">
        <div class="contanier" @click.stop="()=>{return false}" id="editbox">
            <input class="form-control" hidden :value="editCourse.cid">
            <div class="row">
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">课程名</span>
                <input class="form-control" v-model="editCourse.name">
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">教师id</span>
                <input class="form-control" v-model="editCourse.teacher_id">
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">上课时间</span>
                <input class="form-control" v-model="editCourse.lessons">
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">教室</span>
                <input class="form-control" v-model="editCourse.classroom">
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">课程类型</span>
                <select class="form-control" v-model="editCourse.ctype">
                    <option value="专业必修">专业必修</option>
                    <option value="专业选修">专业选修</option>
                    <option value="公共必修">公共必修</option>
                    <option value="公共选修">公共选修</option>
                </select>
                
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">学期</span>
                <input class="form-control" v-model="editCourse.session" type="date">
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">最大人数</span>
                <input type="number" class="form-control" v-model="editCourse.max">
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">学分</span>
                <input type="number" class="form-control" v-model="editCourse.credit">
            </div>
            <button class="btn btn-primary col mt-3 mb-3" @click="updateCourse" href="#">保存</button>
            </div>
        </div>
    </div>
    <button @click="showEditBox({},false)" class="btn btn-outline-primary">添加课程</button>
    <table class="table table-sm" style="text-align: center;">
        <thead class="table-dark">
            <tr>
                <th>课程号</th>
                <th>课程名</th>
                <th>教师号</th>
                <th>教师姓名</th>
                <th>上课时间</th>
                <th>教室</th>
                <th>课程类型</th>
                <th>学期</th>
                <th>已选人数/最大人数</th>
                <th>学分</th>
                <th>状态</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(item, index) in courses" :key="index">
                <td>{{item.cid}}</td>
                <td>{{item.name}}</td>
                <td>{{item.teacher_id}}</td>
                <td>{{item.teacher_name}}</td>
                <td>{{item.times}}</td>
                <td>{{item.classroom}}</td>
                <td>{{item.ctype}}</td>
                <td>{{item.session}}</td>
                <td>{{item.selected}}/{{item.max}}</td>
                <td>{{item.credit}}</td>
                <td>{{item.selectable?'选课中':'选课结束'}}</td>
                <td>
                    <a @click="showEditBox(item,true)" href="#">编辑</a>/
                    <a v-if="item.selectable" href="#" @click="toggleSelectable(item.cid,item.selectable)">结束选课</a>
                    <a v-else @click="toggleSelectable(item.cid,item.selectable)" href="#">开始选课</a>/
                    <a @click="deleteCourse(item.cid)" href="#">删除</a>
                </td>
            </tr>
        </tbody>
    </table>
</template>
<script>
import { API, callAPI } from '@/util/api'

export default {
    name:'admin-course',
    props:['user'],
    data(){
        return{
            courses:[],
            editCourse:{},
            edit:false
        }
    },
    mounted(){
        this.loadCourses()
    },
    methods:{
        loadCourses(){
            callAPI(API.course.getAll,{},res=>{
                this.courses=res.data
                for(let i=0;i<this.courses.length;i++){
                    let lessons=this.courses[i].lessons.split(';')
                    this.courses[i].times=''
                    for(let j=0;j<lessons.length;j++){
                        let dt=lessons[j].split('&')
                        this.courses[i].times+=`周${dt[0]}第${dt[1]}节`
                        if(j!=lessons.length-1)this.courses[i].times+='/'
                    }
                    callAPI(API.sc.getSelected,{cid:this.courses[i].cid},res=>{
                        this.courses[i].selected=res.data
                    })
                }
            })
        },
        deleteCourse(id){
            if(confirm(`你确定要删除课程${id}吗`)){
                callAPI(API.course.delete,{cid:id},()=>{
                    this.loadCourses()
                })
            }
        },
        toggleSelectable(id,v){
            callAPI(API.course.setSelectable,{cid:id,selectable:!v},()=>{
                this.loadCourses()
            })
        },
        showEditBox(item,edit){
            if(!edit){
                this.editCourse={cid:-1,add:true}
            }else{
                this.editCourse=item
                this.editCourse.add=false
            }
            this.edit=true
        },
        closeEditBox(){
            this.editCourse=null
            this.edit=false
        },
        updateCourse(){
            callAPI(API.course.update,this.editCourse,()=>{
                this.closeEditBox()
                this.loadCourses()
            })
        }
    }
}
</script>
