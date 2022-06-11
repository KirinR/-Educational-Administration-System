<template>
    <table class="table table-sm">
        <thead class="table-dark">
            <tr>
                <th>学期</th>
                <th>课程号</th>
                <th>课程名</th>
                <th>课程类型</th>
                <th>学分</th>
                <th>平时成绩</th>
                <th>期末成绩</th>
                <th>总成绩</th>
                <th>绩点</th>
                <th>通过否</th>
            </tr>
        </thead>
        <tbody>
            <tr v-for="(item, index) in scs" :key="index">
                <td>{{item.session}}</td>
                <td>{{item.course_id}}</td>
                <td>{{item.course_name}}</td>
                <td>{{item.ctype}}</td>
                <td>{{item.credit}}</td>
                <td>{{item.usual_grade}}</td>
                <td>{{item.final_grade}}</td>
                <td>{{item.grade}}</td>
                <td>{{item.gpa}}</td>
                <td>{{item.grade>=60?'是':'否'}}</td>
            </tr>
        </tbody>
    </table>
</template>
<script>
import { API, callAPI } from '@/util/api'

export default {
    name:'student-grade',
    props:['user'],
    mounted(){
        this.loadSCs()
    },
    data(){
        return{
            scs:[]
        }
    },
    methods:{
        loadSCs(){
            callAPI(API.sc.getMySCs,{},res=>{
                    this.scs=res.data
                    for(let i=0;i<this.scs.length;i++){
                        callAPI(API.course.get,{cid:this.scs[i].course_id},res=>{
                                this.scs[i].session=res.data.session
                                this.scs[i].credit=res.data.credit
                                this.scs[i].ctype=res.data.ctype
                        })
                    }
            })
        }
    }
}
</script>