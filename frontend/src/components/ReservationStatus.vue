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

