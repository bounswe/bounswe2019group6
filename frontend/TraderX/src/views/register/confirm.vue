<script>
import { Message } from 'element-ui'

export default {
  created() {
    const { query } = this.$route

    this.$store.dispatch('user/confirm', query)
      .then(() => {
        Message.success('Confirmation successful, you can log in now')
        this.$router.push({ path: '/login' })
      })
      .catch(() => {
        Message.error('An error occurred, your account is not confirmed!')
        this.$router.push({ path: '/login' })
      })
  },
  render: function(h) {
    return h() // avoid warning message
  }
}
</script>
