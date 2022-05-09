<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("services") }}</h3>
        <!-- <TabView v-model:activeIndex="tabActiveIndexList">
          <TabPanel :header="$t('servicesList')">
            <Toolbar>
              <template #start>
                <Button :label="$t('new')" icon="bx bx-plus bx-sm" class="p-button-success mx-1" @click="createService()" />
                <Button :label="$t('delete')" icon="bx bxs-trash bx-sm" class="p-button-danger mx-1" @click="servicesToolbar('delete')" />
              </template>
            </Toolbar>
            <DataTable :value="services" filterDisplay="menu" dataKey="_id" :loading="loading" scrollDirection="vertical"
            v-model:selection="selectedServices" class="p-datatable-gridlines" :rowHover="true"
            responsiveLayout="scroll" :scrollable="false" scrollHeight="50vh" paginatorPosition="top"
            :paginator="true" :rows="20" :rowsPerPageOptions="[10,20,50,100,500]" :pageLinkSize="5">
              <template #empty>
                <div class="text-right">
                  {{ $t("noServiceFound") }}
                </div>
              </template>
              <template #loading>
                <div class="text-right">
                  {{ $t("loadingServices") }}
                </div>
              </template>
              <Column selectionMode="multiple" bodyClass="text-center" style="width:5rem;"></Column>
              <Column bodyClass="text-center" style="width:5rem;">
                <template #body="{data}">
                  <div class="flex">
                    <i @click="positionService(1, data._id)" class="bx bx-chevron-up-circle bx-sm position-arrows mx-1" v-tooltip.top="$t('increaseServicePosition')" />
                    <i @click="positionService(-1, data._id)" class="bx bx-chevron-down-circle bx-sm position-arrows mx-1" v-tooltip.top="$t('decreaseServicePosition')" />
                  </div>
                </template>
              </Column>
              <Column field="name" :header="$t('name')" bodyClass="text-center" style="flex: 0 0 10rem">
                <template #body="{data}">
                  {{ data.name }}
                </template>
              </Column>
              <Column field="description" :header="$t('persianName')" headerClass="text-center" bodyClass="text-center" style="flex: 0 0 10rem">
                <template #body="{data}">
                  {{ data.description }}
                </template>
              </Column>
              <Column field="url" :header="$t('url')" bodyClass="text-center" style="flex: 0 0 15rem">
                <template #body="{data}">
                  <a :href="data.url" target="_blank" style="text-decoration: none;">{{ data.url }}</a>
                </template>
              </Column>
              <Column bodyClass="flex justify-content-evenly flex-wrap text-center w-full" style="flex: 0 0 13rem">
                <template #body="{data}">
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="bx bxs-edit bx-sm" class="p-button-rounded p-button-warning p-button-outlined mx-1" @click="editService(data._id)" v-tooltip.top="$t('edit')" />
                  </div>
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="bx bxs-trash bx-sm" class="p-button-rounded p-button-danger p-button-outlined mx-1" @click="deleteService(data._id)" v-tooltip.top="$t('delete')" />
                  </div>
                </template>
              </Column>
            </DataTable>
          </TabPanel>
          <TabPanel v-if="createServiceFlag" :header="$t('createService')">
            <div v-if="createServiceLoader" class="text-center">
              <ProgressSpinner />
            </div>
            <div v-else>
              <TabView v-model:activeIndex="tabActiveIndexCreate">
                <TabPanel :header="$t('basicSettings')">
                </TabPanel>
                <TabPanel :header="$t('accessSettings')">
                  <div class="bootstrap">
                    <div class="card p-2">
                      <div class="card-header bg-light p-2 d-flex justify-content-between align-items-center">
                        <h5>کاربران</h5>
                        <div class="d-flex" style="overflow: auto;">
                          <div style="width: 250px;">
                            <div class="input-group input-group-sm">
                              <InputText id="createServiceB" type="text" class="mx-2" v-model="createServiceB" :placeholder="$t('search')" />
                            </div>
                          </div>
                        </div>
                      </div>
                      <div class="list-group list-group-flush">
                        <div class="list-group-item" v-if="allowedResultQuery.length > 0">
                          <div class="row align-items-start">
                            <div class="card m-2 sm:col-4 md:col-3 lg:col-2 text-center" v-for="user in allowedResultQuery" v-bind:key="user">
                              <div>
                                <label>{{ user._id }}</label>
                                <div class="text-muted text-ellipsis">
                                  <small>{{ user.displayName }}</small>
                                </div>
                                <div class="row">
                                  <div class="col-12">
                                    <Button @click="deleteFromAllowedList(user)" icon="pi pi-times" class="p-button-rounded p-button-danger p-button-outlined p-button-sm" />
                                  </div>
                                </div>
                              </div>
                            </div>
                          </div>
                        </div>
                        <div v-if="allowedResultQuery.length == 0 && searchQueryAllowedList.length != 0" class="list-group-item text-center">
                          <div class="text-center" style="display: grid;">
                            <i class="bx bx-info-circle bx-lg"></i>
                            {{ $t("noUsersFound") }}
                          </div>
                        </div>
                        <div v-if="allowedResultQuery.length == 0 && searchQueryAllowedList.length == 0" class="list-group-item list-group-item-warning text-center">
                          <div class="text-center" style="display: grid;">
                            <i class="bx bx-info-circle bx-lg"></i>
                            {{ $t("listIsEmpty") }}
                          </div>
                        </div>
                      </div>
                    </div>
                  </div>
                </TabPanel>
                <TabPanel :header="$t('authenticationSettings')">
                  <h4>{{ $t("multi-factorAuthentication") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders">{{ $t("activation") }}</label>
                        <Dropdown id="createServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders"
                        v-model="createServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders"
                        :options="multifactorAuthenticationProvidersOptions" optionLabel="name" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.multifactorPolicy.failureMode">Failure Mode</label>
                        <Dropdown id="createServiceBuffer.multifactorPolicy.failureMode" :options="failureModeOptions"
                        v-model="createServiceBuffer.multifactorPolicy.failureMode" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid" style="display: grid;">
                        <label for="createServiceBuffer.multifactorPolicy.bypassEnabled">Bypass Enabled</label>
                        <ToggleButton id="createServiceBuffer.multifactorPolicy.bypassEnabled" onIcon="pi pi-check"
                        v-model="createServiceBuffer.multifactorPolicy.bypassEnabled" offIcon="pi pi-times" />
                      </div>
                    </div>
                  </div>
                  <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="createServiceCheckup()" />
                  <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('createService')" />
                </TabPanel>
                <TabPanel :header="$t('notificationSettings')">
                  <h4>{{ $t("notifications") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.extraInfo.notificationApiURL">{{ $t("apiAddress") }}</label>
                        <InputText id="createServiceBuffer.extraInfo.notificationApiURL" type="text" v-model="createServiceBuffer.extraInfo.notificationApiURL" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.extraInfo.notificationApiKey">{{ $t("apiKey") }}</label>
                        <InputText id="createServiceBuffer.extraInfo.notificationApiKey" type="text" v-model="createServiceBuffer.extraInfo.notificationApiKey" />
                      </div>
                    </div>
                  </div>
                  <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="createServiceCheckup()" />
                  <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('createService')" />
                </TabPanel>
              </TabView>
            </div>
          </TabPanel>
          <TabPanel v-if="editServiceFlag" :header="$t('editService')">
            <div v-if="editServiceLoader" class="text-center">
              <ProgressSpinner />
            </div>
            <div v-else>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editService.id">{{ $t("id") }}<span style="color: red;"> * </span></label>
                    <InputText id="editService.id" type="text" :class="editServiceErrors.id" v-model="editServiceBuffer.id" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editService.name">{{ $t("persianName") }}<span style="color: red;"> * </span></label>
                    <InputText id="editService.name" type="text" :class="editServiceErrors.name" v-model="editServiceBuffer.name" @keypress="persianInputFilter($event)" @paste="persianInputFilter($event)" />
                    <small>{{ $t("inputPersianFilterText") }}</small>
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editService.description">{{ $t("description") }}</label>
                    <Textarea v-model="editServiceBuffer.description" :autoResize="true" rows="3" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editService.usersList">{{ $t("addUsers") }}</label>
                    <div v-if="editServiceUsersLoader" class="text-center">
                      <ProgressSpinner />
                    </div>
                    <PickList v-else v-model="editServiceBuffer.usersListBuffer" dataKey="_id" :dir="$store.state.reverseDirection">
                      <template #sourceheader>
                        {{ $t("nonMemberUsersList") }}
                        <InputText type="text" class="my-2" :placeholder="$t('search')" v-model="editServiceBuffer.nonMemberSearch" @input="editServiceUsersSearch('source')" />
                      </template>
                      <template #targetheader>
                        {{ $t("memberUsersList") }}
                        <InputText type="text" class="my-2" :placeholder="$t('search')" v-model="editServiceBuffer.memberSearch" @input="editServiceUsersSearch('target')" />
                      </template>
                      <template #item="slotProps">
                        <div class="p-caritem">
                          {{ slotProps.item._id }}
                          <div>
                            <span class="p-caritem-vin">{{ slotProps.item.displayName }}</span>
                          </div>
                        </div>
                      </template>
                    </PickList>
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mx-1" @click="editServiceCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mx-1" @click="resetState('editService')" />
            </div>
          </TabPanel>
        </TabView> -->
      </div>
    </div>
  </div>
</template>

<script>
import iziToast from "@/assets/scripts/iziToast.min.js"

export default {
  name: "Services",
  data () {
    return {
      services: [
        {
          serviceId: "https://parsso2.razi.ac.ir/cas",
          _id: 1622621368114,
          name: "SSONew1",
          description: "جدید",
          logo: "https://pars-sso.ir/wp-content/uploads/2018/11/logo.png",
          url: "https://parsso2.razi.ac.ir/cas",
          dailyAccess: null,
          ipaddresses: null
        }
      ],
      selectedServices: [],
      multifactorAuthenticationProvidersOptions: [
        {
          value: "",
          name: this.$t("disabled")
        },
        {
          value: "mfa-simple",
          name: this.$t("smsCode")
        },
        {
          value: "mfa-gauth",
          name: this.$t("oneTimePassword")
        },
        {
          value: "mfa-u2f",
          name: this.$t("hardwareToken")
        }
      ],
      failureModeOptions: [
        "NONE",
        "CLOSED",
        "OPEN",
        "PHANTOM"
      ],
      searchQueryAllowedList: [],
      allowedResultQuery: [
        {
          _id: "admin",
          displayName: "ادمین"
        },
        {
          _id: "admin",
          displayName: "ادمین"
        }
      ],
      createServiceErrors: {
        id: "",
        name: ""
      },
      editServiceErrors: {
        id: "",
        name: ""
      },
      createServiceBuffer: {
        name: "",
        serviceId: "",
        metadataLocation: "",
        description: "",
        logo: "",
        informationUrl: "",
        privacyUrl: "",
        logoutType: "",
        logoutUrl: "",
        contacts: "",
        accessStrategy: {
          requiredAttributes: {
            uid: [
              "java.util.HashSet",
              []
            ],
            ou: ""
          },
          rejectedAttributes: {
            uid: [
              "java.util.HashSet",
              []
            ]
          }
        },
        multifactorPolicy: {
          multifactorAuthenticationProviders: {
            value: "",
            name: this.$t("disabled")
          },
          bypassEnabled: false,
          failureMode: null
        },
        extraInfo: {
          notificationApiURL: null,
          notificationApiKey: null
        }
      },
      editServiceBuffer: {
        id: "",
        name: "",
        description: "",
        usersCount: null,
        usersList: [[], []],
        usersListBuffer: [[], []],
        memberSearch: "",
        nonMemberSearch: "",
        usersAddList: [],
        usersRemoveList: []
      },
      tabActiveIndexList: 0,
      tabActiveIndexCreate: 0,
      tabActiveIndexEdit: 0,
      serviceToolbarBuffer: "",
      loading: false,
      createServiceFlag: false,
      editServiceFlag: false,
      createServiceLoader: false,
      editServiceLoader: false,
      persianRex: null
    }
  },
  mounted () {
    this.persianRex = require("persian-rex/dist/persian-rex")
    this.servicesRequestMaster("getServices")
  },
  methods: {
    servicesRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getServices") {
        this.loading = true
        this.axios({
          url: "/api/services/main",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.services = res.data.data
            vm.loading = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "getService") {
        this.editServiceLoader = true
        this.axios({
          url: "/api/services",
          method: "GET",
          params: {
            id: vm.editServiceBuffer.id,
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.editServiceBuffer = res.data.data
            vm.baseServiceId = res.data.data.id
            vm.editServiceLoader = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editServiceLoader = false
            vm.resetState("editService")
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.editServiceLoader = false
          vm.resetState("editService")
        })
      } else if (command === "createService") {
        this.createServiceBuffer.usersAddList = []
        this.createServiceBuffer.usersRemoveList = []
        const usersAddListTemp = this.createServiceBuffer.usersListBuffer[1].filter(a => !this.createServiceBuffer.usersList[1].map(b => b._id).includes(a._id))
        const usersRemoveListTemp = this.createServiceBuffer.usersListBuffer[0].filter(a => !this.createServiceBuffer.usersList[0].map(b => b._id).includes(a._id))
        for (const i in usersAddListTemp) {
          this.createServiceBuffer.usersAddList.push(usersAddListTemp[i]._id)
        }
        for (const i in usersRemoveListTemp) {
          this.createServiceBuffer.usersRemoveList.push(usersRemoveListTemp[i]._id)
        }
        this.createServiceLoader = true
        this.axios({
          url: "/api/services",
          method: "POST",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            id: vm.createServiceBuffer.id,
            name: vm.createServiceBuffer.name,
            description: vm.createServiceBuffer.description
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 201) {
            this.axios({
              url: "/api/users/service/" + vm.createServiceBuffer.id,
              method: "PUT",
              headers: { "Content-Type": "application/json" },
              data: JSON.stringify({
                add: vm.createServiceBuffer.usersAddList,
                remove: vm.createServiceBuffer.usersRemoveList
              }).replace(/\\\\/g, "\\")
            }).then((res1) => {
              if (res1.data.status.code === 200) {
                vm.createServiceLoader = false
                vm.resetState("createService")
                vm.servicesRequestMaster("getServices")
              } else if (res1.data.status.code === 207) {
                vm.alertPromptMaster(res1.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
                vm.createServiceLoader = false
                vm.resetState("createService")
                vm.servicesRequestMaster("getServices")
              } else {
                vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                vm.createServiceLoader = false
                vm.resetState("createService")
                vm.servicesRequestMaster("getServices")
              }
            }).catch(() => {
              vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
              vm.createServiceLoader = false
              vm.resetState("createService")
              vm.servicesRequestMaster("getServices")
            })
          } else if (res.data.status.code === 302) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createServiceLoader = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createServiceLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.createServiceLoader = false
        })
      } else if (command === "editService") {
        this.editServiceBuffer.usersAddList = []
        this.editServiceBuffer.usersRemoveList = []
        const usersAddListTemp = this.editServiceBuffer.usersListBuffer[1].filter(a => !this.editServiceBuffer.usersList[1].map(b => b._id).includes(a._id))
        const usersRemoveListTemp = this.editServiceBuffer.usersListBuffer[0].filter(a => !this.editServiceBuffer.usersList[0].map(b => b._id).includes(a._id))
        for (const i in usersAddListTemp) {
          this.editServiceBuffer.usersAddList.push(usersAddListTemp[i]._id)
        }
        for (const i in usersRemoveListTemp) {
          this.editServiceBuffer.usersRemoveList.push(usersRemoveListTemp[i]._id)
        }
        this.editServiceLoader = true
        this.axios({
          url: "/api/services/" + vm.baseServiceId,
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            id: vm.editServiceBuffer.id,
            name: vm.editServiceBuffer.name,
            description: vm.editServiceBuffer.description
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            this.axios({
              url: "/api/users/service/" + vm.editServiceBuffer.id,
              method: "PUT",
              headers: { "Content-Type": "application/json" },
              params: {
                lang: langCode
              },
              data: JSON.stringify({
                add: vm.editServiceBuffer.usersAddList,
                remove: vm.editServiceBuffer.usersRemoveList
              }).replace(/\\\\/g, "\\")
            }).then((res1) => {
              if (res1.data.status.code === 200) {
                vm.editServiceLoader = false
                vm.resetState("editService")
                vm.servicesRequestMaster("getServices")
              } else if (res1.data.status.code === 207) {
                vm.alertPromptMaster(res1.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
                vm.editServiceLoader = false
                vm.resetState("editService")
                vm.servicesRequestMaster("getServices")
              } else {
                vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                vm.editServiceLoader = false
                vm.resetState("editService")
                vm.servicesRequestMaster("getServices")
              }
            }).catch(() => {
              vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
              vm.editServiceLoader = false
              vm.resetState("editService")
              vm.servicesRequestMaster("getServices")
            })
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editServiceLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.editServiceLoader = false
        })
      } else if (command === "deleteService") {
        const selectedServiceList = [this.serviceToolbarBuffer]
        this.loading = true
        this.axios({
          url: "/api/services",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: selectedServiceList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 204) {
            vm.loading = false
            vm.servicesRequestMaster("getServices")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "deleteServices") {
        const selectedServicesList = []
        for (const x in this.selectedServices) {
          selectedServicesList.push(this.selectedServices[x].id)
        }
        this.loading = true
        this.axios({
          url: "/api/services",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: selectedServicesList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 204) {
            vm.loading = false
            vm.servicesRequestMaster("getServices")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "editPosition") {
        this.loading = true
        this.axios({
          url: "/api/services/position/" + vm.serviceToolbarBuffer.id,
          method: "GET",
          params: {
            value: vm.serviceToolbarBuffer.change,
            lang: langCode
          }
        }).then(() => {
          vm.loading = false
          vm.servicesRequestMaster("getServices")
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      }
    },
    alertPromptMaster (title, message, icon, background) {
      let rtl = true
      if (this.$store.state.direction === "ltr") {
        rtl = false
      }
      iziToast.show({
        title: title,
        message: message,
        position: "center",
        icon: "pi " + icon,
        backgroundColor: background,
        transitionIn: "fadeInLeft",
        rtl: rtl,
        layout: 2,
        timeout: false,
        progressBar: false,
        buttons: [
          [
            "<button class='service-notification-button my-3' style='border-radius: 6px;'>" + this.$t("confirm") + "</button>",
            function (instance, toast) {
              instance.hide({
                transitionOut: "fadeOutRight"
              }, toast)
            }
          ]
        ]
      })
    },
    confirmPromptMaster (title, message, icon, background, functionCallback, callBackParameter) {
      let rtl = true
      if (this.$store.state.direction === "ltr") {
        rtl = false
      }
      iziToast.show({
        title: title,
        message: message,
        position: "center",
        icon: "pi " + icon,
        backgroundColor: background,
        transitionIn: "fadeInLeft",
        rtl: rtl,
        layout: 2,
        timeout: false,
        progressBar: false,
        buttons: [
          [
            "<button class='service-notification-button mx-3 my-3' style='background: #22C55E; color: #FFFFFF; border-radius: 6px;'>" + this.$t("yes") + "</button>",
            function (instance, toast) {
              instance.hide({
                transitionOut: "fadeOutRight"
              }, toast)
              functionCallback(callBackParameter)
            }
          ],
          [
            "<button class='service-notification-button mx-3 my-3' style='background: #EF4444; color: #FFFFFF; border-radius: 6px;'>" + this.$t("no") + "</button>",
            function (instance, toast) {
              instance.hide({
                transitionOut: "fadeOutRight"
              }, toast)
            }
          ]
        ]
      })
    },
    createService () {
      this.createServiceFlag = true
      this.tabActiveIndexList = 1
    },
    createServiceCheckup () {
      let errorCount = 0
      if (this.createServiceBuffer.id === "") {
        this.createServiceErrors.id = "p-invalid"
        errorCount += 1
      } else {
        this.createServiceErrors._id = ""
      }
      if (this.createServiceBuffer.name === "") {
        this.createServiceErrors.name = "p-invalid"
        errorCount += 1
      } else {
        this.createServiceErrors.name = ""
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.servicesRequestMaster("createService")
      }
    },
    editService (id) {
      this.editServiceBuffer.id = id
      this.editServiceFlag = true
      if (this.createServiceFlag) {
        this.tabActiveIndex = 2
      } else {
        this.tabActiveIndex = 1
      }
      this.servicesRequestMaster("getService")
      this.servicesRequestMaster("getUsersEditService")
    },
    editServiceCheckup () {
      let errorCount = 0
      if (this.editServiceBuffer.id === "") {
        this.editServiceErrors.id = "p-invalid"
        errorCount += 1
      } else {
        this.editServiceErrors.id = ""
      }
      if (this.editServiceBuffer.name === "") {
        this.editServiceErrors.name = "p-invalid"
        errorCount += 1
      } else {
        this.editServiceErrors.name = ""
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.servicesRequestMaster("editService")
      }
    },
    deleteService (id) {
      this.serviceToolbarBuffer = id
      this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + String(id), "pi-question-circle", "#F0EAAA", this.servicesRequestMaster, "deleteService")
    },
    positionService (change, id) {
      this.serviceToolbarBuffer = {
        id: id,
        change: change
      }
      this.servicesRequestMaster("editPosition")
    },
    servicesToolbar (action) {
      if (this.selectedServices.length > 0) {
        if (action === "delete") {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + this.$t("services"), "pi-question-circle", "#F0EAAA", this.servicesRequestMaster, "deleteServices")
        } else if (action === "expirePassword") {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("expirePassword") + " " + this.$t("services"), "pi-question-circle", "#F0EAAA", this.servicesRequestMaster, "expireServicesPasswords")
        }
      } else {
        this.alertPromptMaster(this.$t("noServiceSelected"), "", "pi-exclamation-triangle", "#FDB5BA")
      }
    },
    resetState (command) {
      if (command === "createService") {
        this.tabActiveIndex = 0
        this.createServiceFlag = false
        this.createServiceBuffer = {
          id: "",
          name: "",
          description: "",
          usersCount: null,
          usersList: [[], []]
        }
      } else if (command === "editService") {
        this.tabActiveIndex = 0
        this.editServiceFlag = false
        this.editServiceBuffer = {
          id: "",
          name: "",
          description: "",
          usersCount: null,
          usersList: [[], []]
        }
      }
    },
    englishInputFilter ($event) {
      if ($event.type === "keypress") {
        const keyCode = ($event.keyCode ? $event.keyCode : $event.which)
        if (keyCode < 48 || keyCode > 122) {
          $event.preventDefault()
        } else if (keyCode > 57 && keyCode < 65) {
          $event.preventDefault()
        } else if (keyCode > 90 && keyCode < 97) {
          $event.preventDefault()
        }
      } else if ($event.type === "paste") {
        const text = $event.clipboardData.getData("text")
        for (let i = 0; i < text.length; ++i) {
          if (text[i].charCodeAt(0) < 48 || text[i].charCodeAt(0) > 122) {
            $event.preventDefault()
            break
          } else if (text[i].charCodeAt(0) > 57 && text[i].charCodeAt(0) < 65) {
            $event.preventDefault()
            break
          } else if (text[i].charCodeAt(0) > 90 && text[i].charCodeAt(0) < 97) {
            $event.preventDefault()
            break
          }
        }
      }
    },
    persianInputFilter ($event) {
      if ($event.type === "keypress") {
        const key = ($event.key ? $event.key : $event.which)
        const keyCode = ($event.keyCode ? $event.keyCode : $event.which)
        if (keyCode < 48 || keyCode > 57) {
          if (keyCode > 32 && keyCode < 65) {
            $event.preventDefault()
          } else if (keyCode > 90 && keyCode < 97) {
            $event.preventDefault()
          } else if (keyCode > 122 && keyCode < 127) {
            $event.preventDefault()
          } else if (!this.persianRex.text.test(key)) {
            $event.preventDefault()
          }
        }
      } else if ($event.type === "paste") {
        const text = $event.clipboardData.getData("text")
        for (let i = 0; i < text.length; ++i) {
          if (text[i].charCodeAt(0) < 48 || text[i].charCodeAt(0) > 57) {
            if (text[i].charCodeAt(0) > 32 && text[i].charCodeAt(0) < 65) {
              $event.preventDefault()
              break
            } else if (text[i].charCodeAt(0) > 90 && text[i].charCodeAt(0) < 97) {
              $event.preventDefault()
              break
            } else if (text[i].charCodeAt(0) > 122 && text[i].charCodeAt(0) < 127) {
              $event.preventDefault()
              break
            } else if (!this.persianRex.text.test(text[i])) {
              $event.preventDefault()
              break
            }
          }
        }
      }
    }
  }
}
</script>

<style scoped>
.p-toolbar {
  border-radius: 6px 6px 0 0;
}
.p-column-header-content {
  justify-content: space-between !important;
}
.position-arrows {
  cursor: pointer;
}
.position-arrows:hover {
  color: #007bff;
}
</style>
