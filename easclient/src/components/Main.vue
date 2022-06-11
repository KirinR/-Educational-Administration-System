<template>
    <AGrade v-if="aView('grade')" :user="user" v-cloak></AGrade>
    <AUser v-if="aView('user')" :user="user" v-cloak></AUser>
    <ASelectCourse v-if="aView('selectCourse')" :user="user" v-cloak></ASelectCourse>
    <ACourse v-if="aView('course')" :user="user" v-cloak></ACourse>
    <SCourse v-if="sView('course')" :user="user" v-cloak></SCourse>
    <SGrade v-if="sView('grade')" :user="user" v-cloak></SGrade>
    <SSelectCourse v-if="sView('selectCourse')" :user="user" v-cloak></SSelectCourse>
    <TCourse v-if="tView('course')" :user="user" v-cloak></TCourse>
    <TGrade v-if="tView('grade')" :user="user" v-cloak></TGrade>
</template>
<script>
import { router } from "@/util/router"
import ACourse from "./administrator/aCourse.vue"
import AUser from "./administrator/aUser.vue"
import ASelectCourse from "./administrator/aSelectCourse.vue"
import AGrade from "./administrator/aGrade.vue"
import SCourse from "./student/sCourse.vue"
import SGrade from "./student/sGrade.vue"
import SSelectCourse from "./student/sSelectCourse.vue"
import TCourse from "./teacher/tCourse.vue"
import TGrade from "./teacher/tGrade.vue"
export default{
    name: "main-component",
    props: ["router", "user"],
    components: { SCourse, TCourse, ACourse, AUser, SGrade, SSelectCourse, ASelectCourse, AGrade ,TGrade },
    methods:{
        sView(r){
            return router.student[r]===this.router
        },
        tView(r){
            return router.teacher[r]===this.router
        },
        aView(r){
            return router.administrator[r]===this.router
        }
    }
}
</script>