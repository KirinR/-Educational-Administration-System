<template>
    <table class="table table-sm" style="text-align: center;">
        <thead class="table-dark">
            <tr>
                <th>节次/周次</th>
                <th>周一</th>
                <th>周二</th>
                <th>周三</th>
                <th>周四</th>
                <th>周五</th>
                <th>周六</th>
                <th>周日</th>
            </tr>
        </thead>
        <tbody style="font-size: 10px;">
            <tr v-for="(i, idx) in courses" :key="idx">
                <th><span style="font-size:14px">{{ i.order }}</span><br>{{ i.time }}</th>
                <td v-for="(c, idx) in i.courseList" :key="idx"><b
                        style="font-size:14px">{{ c.name }}</b><br>{{ c.classroom }}</td>
            </tr>
        </tbody>
    </table>
</template>
<script>
import { API, callAPI } from '@/util/api'

export default {
    name: 'teacher-course',
    props: ['user'],
    mounted(){
        this.loadCourses()
    },
    data() {
        return {
            courses: [
                { order: '第一节', time: '8:00-8:45', courseList: [] },
                { order: '第二节', time: '8:50-9:35', courseList: [] },
                { order: '第三节', time: '9:50-10:35', courseList: [] },
                { order: '第四节', time: '10:40-11:25', courseList: [] },
                { order: '第五节', time: '13:00-13:45', courseList: [] },
                { order: '第六节', time: '13:50-14:35', courseList: [] },
                { order: '第七节', time: '14:50-15:35', courseList: [] },
                { order: '第八节', time: '15:40-16:25', courseList: [] },
                { order: '第九节', time: '18:00-18:45', courseList: [] },
                { order: '第十节', time: '18:50-19:35', courseList: [] },
                { order: '第十一节', time: '19:40-20:25', courseList: [] },
            ]
        }
    },
    methods: {
        loadCourses() {
            for (let i = 0; i < this.courses.length; i++) {
                this.courses[i].courseList = []
                for (let j = 0; j < 7; j++)this.courses[i].courseList.push({})
            }
            callAPI(API.course.getTeacherCourses, { teacher_id: this.user.uid }, res => {
                var cs = res.data
                for (let i = 0; i < cs.length; i++) {
                    let lessons = cs[i].lessons.split(';')
                    for (let j = 0; j < lessons.length; j++) {
                        let s = lessons[j].split('&')
                        this.courses[Number(s[1]) - 1].courseList[Number(s[0]) - 1] = cs[i]
                    }
                }
            })
        }
    }
}
</script>