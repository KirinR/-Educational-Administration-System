<template>
    <div id="hover" v-if="edit" @click="closeEditBox">
    <div class="contanier" @click.stop="()=>{return false}" id="editbox">
        <input class="form-control" hidden :value="user.uid">
        <div class="row">
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">学号</span>
                <input class="form-control" v-model="student_id">
            </div>
            <div class="col input-group mt-3 mb-3">
                <span class="input-group-text">课程号</span>
                <input class="form-control" v-model="course_id">
            </div>
            <button class="btn btn-primary col mt-3 mb-3" @click="selectCourse" href="#">保存</button>
        </div>
    </div>
    </div>
    <button @click="showEditBox()" class="btn btn-outline-primary">添加选课记录</button>
    <table class="table table-sm" style="text-align: center;">
        <thead class="table-dark">
            <tr>
                <th>课程号</th>
                <th>课程名</th>
                <th>教师号</th>
                <th>教师姓名</th>
                <th>学生号</th>
                <th>学生姓名</th>
                <th>课程类型</th>
                <th>学期</th>
                <th>学分</th>
                <th>教室</th>
                <th>上课时间</th>
                <th>当前人数/总人数</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(item, index) in scs" :key="index">
                <td>{{item.course_id}}</td>
                <td>{{item.course_name}}</td>
                <td>{{item.teacher_id}}</td>
                <td>{{item.teacher_name}}</td>
                <td>{{item.student_id}}</td>
                <td>{{item.student_name}}</td>
                <td>{{item.ctype}}</td>
                <td>{{item.session}}</td>
                <td>{{item.credit}}</td>
                <td>{{item.classroom}}</td>
                <td>{{item.times}}</td>
                <td>{{item.cur}}/{{item.max}}</td>
                <td><a href="#" @click="deleteSC(item.scid)">删除</a></td>
            </tr>
        </tbody>
    </table>
</template>
<script>
import { API, callAPI } from '@/util/api'

export default{
    name:'admin-selectCourse',
    props:['user'],
    data(){
        return{
            scs:[],
            sc:{},
            edit:false
        }
    },
    mounted(){
        this.loadSCs()
    },
    methods:{
        loadSCs(){
            callAPI(API.sc.getAll,{},res=>{
                this.scs=res.data
                for(let i=0;i<this.scs.length;i++){
                    callAPI(API.course.get,{cid:this.scs[i].course_id},res=>{
                        this.scs[i].teacher_id=res.data.teacher_id
                        this.scs[i].teacher_name=res.data.teacher_name
                        this.scs[i].ctype=res.data.ctype
                        this.scs[i].session=res.data.session
                        this.scs[i].credit=res.data.credit
                        this.scs[i].classroom=res.data.classroom
                        this.scs[i].max=res.data.max
                        let lessons=res.data.lessons.split(';')
                        this.scs[i].times=''
                        for(let j=0;j<lessons.length;j++){
                            let dt=lessons[j].split('&')
                            this.scs[i].times+=`周${dt[0]}第${dt[1]}节`
                            if(j!=lessons.length-1)this.scs[i].times+='/'
                        }
                    })
                    callAPI(API.sc.getSelected,{cid:this.scs[i].course_id},res=>{
                        this.scs[i].cur=res.data
                    })
                }
            })
        },
        deleteSC(id){
            if(confirm(`你确定要删除选课记录${id}吗?`)){
                callAPI(API.sc.delete,{scid:id},()=>{
                    this.loadSCs()
                })
            }
        },
        showEditBox(){
            this.edit=true
        },
        closeEditBox(){
            this.edit=false
        },
        selectCourse(){
            callAPI(API.sc.selectCourse,{student_id:this.student_id,course_id:this.course_id},()=>{
                alert('选课成功')
                this.loadSCs()
            })
        },
    }
}
</script>