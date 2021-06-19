<template>

<v-data-table
    :headers="headers"
    :items="reservationStatus"
    :items-per-page="5"
    class="elevation-1"
  ></v-data-table>

</template>

<script>
  const axios = require('axios').default;

  export default {
    name: 'ReservationStatus',
    props: {
      value: Object,
      editMode: Boolean,
      isNew: Boolean
    },
    data: () => ({
        headers: [
            { text: "id", value: "id" },
            { text: "reservationId", value: "reservationId" },
            { text: "vaccineId", value: "vaccineId" },
            { text: "hospitalId", value: "hospitalId" },
            { text: "reservationStatus", value: "reservationStatus" },
            { text: "reservationDate", value: "reservationDate" },
            { text: "vaccineName", value: "vaccineName" },
            { text: "vaccineDate", value: "vaccineDate" },
            { text: "vaccineValidationDate", value: "vaccineValidationDate" },
            { text: "hospitalName", value: "hospitalName" },
            { text: "hospitalLocation", value: "hospitalLocation" },
            { text: "userName", value: "userName" },
            { text: "userPhone", value: "userPhone" },
        ],
        reservationStatus : [],
    }),
    async created() {
      var temp = await axios.get(axios.backend + '/reservationstatuses')

      this.reservationStatus = temp.data._embedded.reservationstatuses;

    },
    methods: {
    }
  }
</script>

