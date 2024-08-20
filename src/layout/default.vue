<script setup lang="ts" allowJs="true">

</script>

<template>
  <div id="container">
    <div class="box--1">
      <div class="function_head">
        <NuxtLink to="/">
          <div class="logo__project">
            <a>

              <!-- <font-awesome-icon icon="fa-brands fa-slack" style="color: #46c3aa; font-size: 35px;" /> -->

              <!-- <p>perplexity</p> -->
              <img class="logo_company" src="@/image/logo/logo_company(2).png" alt="">
              <img class="title_company" src="@/image/logo/title_company(1).png" alt="">

            </a>
          </div>
        </NuxtLink>
        <!-- <div class="search__ask">
            <input class="search" type="text" placeholder="New Thead...">
        </div> -->
        <div class="function__app">
          <div class="function__list">
            <NuxtLink to="/Home">
              <a class="function__home">
                <font-awesome-icon icon="fa-solid fa-magnifying-glass"/>
                Trang chủ
              </a>
            </NuxtLink>
          </div>
          <div class="function__list">
            <NuxtLink to="/Discover">
              <a class="function__home">
                <font-awesome-icon icon="fa-regular fa-compass"/>
                Xu hướng
              </a>
            </NuxtLink>

          </div>
          <div class="function__list">
            <NuxtLink to="/Library">
              <a class="function__home">
                <font-awesome-icon icon="fa-brands fa-stack-overflow"/>
                Lịch sử
              </a>
            </NuxtLink>
          </div>
        </div>
      </div>

      <div class="function_footer">
        <div class="border_bt_footer">
          <div class="introduction">
            <h5>Trải nghiệm nâng cao</h5>
            <p>
              Nâng cấp lên GPT-4, để có thể tải lên nhiều tệp hơn và trải nghiệm tốt hơn.
            </p>
          </div>
          <div class="function__list_footer">
            <button class="function__learn">
              <font-awesome-icon icon="fa-solid fa-arrow-up-right-from-square"/>
              Xem Thêm
            </button>
          </div>
          <div class="function__list_footer">
            <a href="">
              <div class="function__account">
                <img src="@/image/logo/connect_face.png" alt="">
                <p>phanminhvu_t65@devthanh</p>
              </div>
            </a>
          </div>
        </div>
        <div class="connect">
          <div class="path_connect">
            <button class="contact contact__download">
              <font-awesome-icon icon="fa-solid fa-download"/>
              Tải xuống
            </button>
            <button class="contact">
              <font-awesome-icon icon="fa-brands fa-x-twitter"/>
            </button>
            <button class="contact">
              <font-awesome-icon icon="fa-brands fa-github-alt"/>
            </button>
          </div>
        </div>
      </div>
    </div>

    <div id="main-page" class="box--2">
      <slot></slot>

    </div>
  </div>
</template>

<style scoped>

</style>