<template>
    <table class="table table-sm">
        <thead class="table-dark">
            <tr>
                <th>课程号</th>
                <th>课程名</th>
                <th>学期</th>
                <th>上课时间</th>
                <th>教室</th>
                <th>课程类型</th>
                <th>学分</th>
                <th>授课教师</th>
                <th>已选人数/最大人数</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(item, index) in courses" :key="index">
                <td>{{item.cid}}</td>
                <td>{{item.name}}</td>
                <td>{{item.session}}</td>
                <td>{{item.times}}</td>
                <td>{{item.classroom}}</td>
                <td>{{item.ctype}}</td>
                <td>{{item.credit}}</td>
                <td>{{item.teacher_name}}</td>
                <td>{{item.cur}}/{{item.max}}</td>
                <td>
                    <a href="#" v-if="item.scid==null" @click="selectCourse(item.cid)" v-cloak>选课</a>
                    <a href="#" v-else @click="deleteCourse(item.scid)" v-cloak>退课</a>
                </td>
            </tr>
        </tbody>
    </table>
</template>
<script>
import { API, callAPI } from '@/util/api'

export default {
    name:'student-selectCourse',
    props:['user'],
    data(){
        return{
            courses:[]
        }
    },
    mounted(){
        this.loadCourses()
    },  
    methods:{
        loadCourses(){
            callAPI(API.course.getAll,{},res=>{
                this.courses=[]
                for(let i=0;i<res.data.length;i++){
                    if(res.data[i].selectable)this.courses.push(res.data[i])
                }
                for(let i=0;i<this.courses.length;i++){
                    let lessons=this.courses[i].lessons.split(';')
                    this.courses[i].times=''
                    for(let j=0;j<lessons.length;j++){
                        let dt=lessons[j].split('&')
                        this.courses[i].times+=`周${dt[0]}第${dt[1]}节`
                        if(j!=lessons.length-1)this.courses[i].times+='/'
                    }
                    callAPI(API.sc.getIsSelected,{cid:this.courses[i].cid},res=>{
                        this.courses[i].scid=res.data
                    })
                    callAPI(API.sc.getSelected,{cid:this.courses[i].cid},res=>{
                        this.courses[i].cur=res.data
                    })
                }
            })
        },
        selectCourse(cid){
            callAPI(API.sc.selectCourse,{student_id:this.user.uid,course_id:cid},()=>{
                alert('选课成功')
                this.loadCourses()
            })
        },
        deleteCourse(id){
            callAPI(API.sc.delete,{scid:id},()=>{alert('退课成功');this.loadCourses()})
        }
    }
}
</script>