<template>
    <span>选择课程</span>
    <select v-model="course">
        <option v-for="(item, index) in courses" :key="index" :value="index">
            {{item.cid}}:{{item.name}}
        </option>
    </select>
    <select v-model="edit">
        <option value="1">
            编辑模式
        </option>
        <option value="0">
            查阅模式
        </option>
    </select>
    <table class="table table-sm" style="text-align: center;">
        <thead class="table-dark">
            <tr>
                <th>课程号</th>
                <th>课程名</th>
                <th>学生学号</th>
                <th>学生姓名</th>
                <th>学期</th>
                <th>平时成绩</th>
                <th>期末成绩</th>
                <th>总成绩</th>
                <th>GPA</th>
                <th>操作</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(item, index) in scs" :key="index">
                <td>{{item.course_id}}</td>
                <td>{{item.course_name}}</td>
                <td>{{item.student_id}}</td>
                <td>{{item.student_name}}</td>
                <td>{{courses[course].session}}</td>
                <td><input v-model="item.usual_grade" :disabled="edit==0"></td>
                <td><input v-model="item.final_grade" :disabled="edit==0"></td>
                <td><input v-model="item.grade" :disabled="edit==0"></td>
                <td><input v-model="item.gpa" :disabled="edit==0"></td>
                <td><a href="#" @click="save(item)">保存</a></td>
            </tr>
        </tbody>
    </table>
</template>
<script>
import { API, callAPI } from '@/util/api'

export default{
    name:'teacher-grade',
    props:['user'],
    data(){
        return{
            course:-1,
            courses:[],
            scs:[],
            edit:1
        }
    },
    mounted(){
        this.loadCourses()
    },
    watch:{
        course(){
            this.loadSCs()   
        },
    },
    methods:{
        loadCourses(){
            callAPI(API.course.getTeacherCourses,{teacher_id:this.user.uid},res=>{
                this.courses=res.data
                this.course=0
                this.loadSCs()
            })
        },
        loadSCs(){
            callAPI(API.sc.getTeacherSCs,{},res=>{
                let d=res.data
                this.scs=[]
                for(let i=0;i<d.length;i++){
                    if(d[i].course_id==this.courses[this.course].cid){
                        this.scs.push(d[i])
                    }
                }
            })
        },
        save(item){
            if(this.edit==1){
                callAPI(API.sc.setGrade,{
                    scid:item.scid,
                    usual_grade:item.usual_grade,
                    final_grade:item.final_grade,
                    grade:item.grade,
                    GPA:item.gpa
                },res=>{alert(res.message)})
            }
        }
    }
}
</script>