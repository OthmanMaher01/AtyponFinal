<script setup>
import axios from 'axios';
import { useRouter } from 'vue-router';
import { ref } from 'vue';

const form = ref({
  email: '',
  password: '',
  errorMessage: '',
  remember: false,
})
const router = useRouter();
const loginUser = async () => {

  try {
    const response = await axios.post('http://localhost:8000/AtyponDatabase/login', {
      email: form.value.email,
      password: form.value.password,
    });

    if (response.status === 200) {
      router.push('/dashboard');
    } else {
      form.errorMessage = "Invalid credentials!!";
    }
  } catch (error) {
    console.error(error);
    form.errorMessage = "An error occurred. Please try again later.";
  }
};

const isPasswordVisible = ref(false)
</script>

<template>
  <div class="auth-wrapper d-flex align-center justify-center pa-4">
    <VCard
      class="auth-card pa-4 pt-7"
      max-width="448"
    >

      <VCardText class="pt-2 align-center justify-center text-center">
        <h5 class="text-h5 mb-1">
          Atypon Database
        </h5>
        <h6 class="text-h5 mb-1">
          Login
        </h6>
      </VCardText>

      <VCardText>
        <VForm @submit.prevent="loginUser">
          <VRow>
            <!-- email -->
            <VCol cols="12">
              <VTextField
                v-model="form.email"
                label="username"
                type="email"
              />
            </VCol>

            <!-- password -->
            <VCol cols="12">
              <VTextField
                v-model="form.password"
                label="Password"
                placeholder="············"
                :type="isPasswordVisible ? 'text' : 'password'"
                :append-inner-icon="isPasswordVisible ? 'bx-hide' : 'bx-show'"
                @click:append-inner="isPasswordVisible = !isPasswordVisible"
              />

              <!-- remember me checkbox -->
              <div class="d-flex align-center justify-space-between flex-wrap mt-1 mb-4">

              </div>

              <!-- login button -->
              <VBtn
                block
                type="submit"
              >
                Login
              </VBtn>
            </VCol>

            <!-- create account -->
            <VCol
              cols="12"
              class="text-center text-base"
            >
              <span>New on our platform?</span>
              <RouterLink
                class="text-primary ms-2"
                to="/register"
              >
                Create an account
              </RouterLink>
            </VCol>
            <div v-if="form.errorMessage" class="error-message">{{ form.errorMessage }}</div>

          </VRow>
        </VForm>
      </VCardText>
    </VCard>
  </div>
</template>

<style lang="scss">
@use "@core/scss/template/pages/page-auth.scss";
</style>
