
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import ReservationManager from "./components/ReservationManager"

import VaccineManager from "./components/VaccineManager"

import HospitalManager from "./components/HospitalManager"


import ReservationStatus from "./components/ReservationStatus"
export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/Reservation',
                name: 'ReservationManager',
                component: ReservationManager
            },

            {
                path: '/Vaccine',
                name: 'VaccineManager',
                component: VaccineManager
            },

            {
                path: '/Hospital',
                name: 'HospitalManager',
                component: HospitalManager
            },


            {
                path: '/ReservationStatus',
                name: 'ReservationStatus',
                component: ReservationStatus
            },


    ]
})
