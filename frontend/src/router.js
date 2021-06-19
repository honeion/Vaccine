
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import ReservationManagementManager from "./components/ReservationManagementManager"

import VaccineManagementManager from "./components/VaccineManagementManager"

import HospitalManagementManager from "./components/HospitalManagementManager"


import ReservationStatus from "./components/ReservationStatus"
export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/ReservationManagement',
                name: 'ReservationManagementManager',
                component: ReservationManagementManager
            },

            {
                path: '/VaccineManagement',
                name: 'VaccineManagementManager',
                component: VaccineManagementManager
            },

            {
                path: '/HospitalManagement',
                name: 'HospitalManagementManager',
                component: HospitalManagementManager
            },


            {
                path: '/ReservationStatus',
                name: 'ReservationStatus',
                component: ReservationStatus
            },


    ]
})
