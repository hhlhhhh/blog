<script setup>
import { onMounted } from 'vue';
import { UserStore }  from './store/index.js'

const userStore = UserStore()

onMounted(() => {
  let user = JSON.parse(localStorage.getItem('user'))
  userStore.setUser(user? user: {})
  let token = localStorage.getItem('token')
  userStore.setToken(token)
})

</script>
  <template>
    <router-view></router-view>
  </template>
<style>
*{
  padding: 0;
  margin: 0;
  box-sizing: border-box;
}
li{
  list-style: none;
}
a{
  text-decoration: none;
  color: black;
  cursor: pointer;
}
em{
  font-style: normal;
}
i{
  font-style: normal;
}
.app-content{
  display: block;
  min-width: 1200px;
}
</style>
