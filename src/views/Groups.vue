<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("groups") }}</h3>
        <TabView v-model:activeIndex="tabActiveIndex">
          <TabPanel :header="$t('groupsList')">
            <Toolbar>
              <template #start>
                <Button :label="$t('new')" icon="pi pi-plus" class="p-button-success mx-1" @click="createGroup()" />
                <Button :label="$t('delete')" icon="pi pi-trash" class="p-button-danger mx-1" @click="groupsToolbar('delete')" />
                <i class="pi pi-bars p-toolbar-separator mx-1" ></i>
                <Button icon="pi pi-calendar-times" class="p-button-warning mx-1" v-tooltip.top="$t('expirePassword')" @click="groupsToolbar('expirePassword')" />
              </template>
              <template #end>
                <Button icon="pi pi-download" class="mx-1" @click="toggleImportGroup($event)" v-tooltip.top="'Import'" />
                <OverlayPanel ref="importGroup">
                  <div class="p-inputgroup mx-1">
                    <Button icon="pi pi-file" @click="importGroupsHelper()" v-tooltip.top="$t('selectFile')" />
                    <input id="importGroupsInput" type="file" @change="importGroups()" class="hidden" accept=".xlsx, .xls, .csv">
                    <Dropdown v-model="importSelectedGroup" :options="groups" optionLabel="name" :placeholder="$t('groups')" />
                  </div>
                </OverlayPanel>
                <Button icon="pi pi-info" class="p-button-secondary mx-1" @click="toggleSampleFile($event)" v-tooltip.top="$t('sample')" />
                <OverlayPanel ref="sampleFile">
                  <Listbox v-model="selectedSampleFile" :options="sampleFiles" @change="getSampleFile" />
                  <form method="GET" action="templates/csv.csv" class="hidden">
                    <Button id="getSampleFileCSV" type="submit" />
                  </form>
                  <form method="GET" action="templates/xls.xls" class="hidden">
                    <Button id="getSampleFileXLS" type="submit" />
                  </form>
                  <form method="GET" action="templates/xlsx.xlsx" class="hidden">
                    <Button id="getSampleFileXLSX" type="submit" />
                  </form>
                </OverlayPanel>
              </template>
            </Toolbar>
            <DataTable :value="groups" dataKey="id" :loading="loading" scrollDirection="vertical"
            v-model:selection="selectedGroups" class="p-datatable-gridlines" :rowHover="true"
            responsiveLayout="scroll" :scrollable="false" scrollHeight="50vh">
              <template #empty>
                <div class="text-right">
                  {{ $t("noGroupsFound") }}
                </div>
              </template>
              <template #loading>
                <div class="text-right">
                  {{ $t("loadingGroups") }}
                </div>
              </template>
              <Column selectionMode="multiple" bodyClass="text-center" style="width:5rem;"></Column>
              <Column field="id" :header="$t('id')" bodyClass="text-center" style="flex: 0 0 12rem">
                <template #body="{data}">
                  {{ data.id }}
                </template>
              </Column>
              <Column field="name" :header="$t('persianName')" bodyClass="text-center" style="flex: 0 0 12rem">
                <template #body="{data}">
                  {{ data.name }}
                </template>
              </Column>
              <Column field="usersCount" :header="$t('usersCount')" bodyClass="text-center" style="flex: 0 0 2rem">
                <template #body="{data}">
                  {{ data.usersCount }}
                </template>
              </Column>
              <Column bodyStyle="display: flex;" bodyClass="flex justify-content-evenly flex-wrap card-container text-center w-full" style="flex: 0 0 13rem">
                <template #body="{data}">
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="pi pi-pencil" class="p-button-rounded p-button-warning p-button-outlined mx-1" @click="editGroup(data.id)" v-tooltip.top="$t('edit')" />
                  </div>
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="pi pi-calendar-times" class="p-button-rounded p-button-info p-button-outlined mx-1" @click="expireGroupPassword(data.id)" v-tooltip.top="$t('expirePassword')" />
                  </div>
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="pi pi-trash" class="p-button-rounded p-button-danger p-button-outlined mx-1" @click="deleteGroup(data.id)" v-tooltip.top="$t('delete')" />
                  </div>
                </template>
              </Column>
            </DataTable>
          </TabPanel>
          <TabPanel v-if="createGroupFlag" :header="$t('createGroup')">
            <div v-if="createGroupLoader" class="text-center">
              <ProgressSpinner />
            </div>
            <div v-else>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createGroup.id">{{ $t("id") }}<span style="color: red;"> * </span></label>
                    <InputText id="createGroup.id" type="text" :class="createGroupErrors.id" v-model="createGroupBuffer.id" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createGroup.name">{{ $t("persianName") }}<span style="color: red;"> * </span></label>
                    <InputText id="createGroup.name" type="text" :class="createGroupErrors.name" v-model="createGroupBuffer.name" @keypress="persianInputFilter($event)" @paste="persianInputFilter($event)" />
                    <small>{{ $t("inputPersianFilterText") }}</small>
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createGroup.description">{{ $t("description") }}</label>
                    <Textarea v-model="createGroupBuffer.description" :autoResize="true" rows="3" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createGroup.usersList">{{ $t("addUsers") }}</label>
                    <div v-if="createGroupUsersLoader" class="text-center">
                      <ProgressSpinner />
                    </div>
                    <PickList v-else v-model="createGroupBuffer.usersListBuffer" dataKey="_id" :dir="$store.state.reverseDirection"
                    @move-to-target="createGroupUsersControl($event, 'moveToTarget')" @move-all-to-target="createGroupUsersControl($event, 'moveToTarget')"
                    @move-to-source="createGroupUsersControl($event, 'moveToSource')" @move-all-to-source="createGroupUsersControl($event, 'moveToSource')">
                      <template #sourceheader>
                        {{ $t("nonMemberUsersList") }}
                        <InputText type="text" class="my-2" :placeholder="$t('search')" v-model="createGroupBuffer.nonMemberSearch" @input="createGroupUsersSearch('source')" />
                      </template>
                      <template #targetheader>
                        {{ $t("memberUsersList") }}
                        <InputText type="text" class="my-2" :placeholder="$t('search')" v-model="createGroupBuffer.memberSearch" @input="createGroupUsersSearch('target')" />
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
              <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="createGroupCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('createGroup')" />
            </div>
          </TabPanel>
          <TabPanel v-if="editGroupFlag" :header="$t('editGroup') + '(' + editGroupBuffer.id + ')'">
            <div v-if="editGroupLoader" class="text-center">
              <ProgressSpinner />
            </div>
            <div v-else>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editGroup.id">{{ $t("id") }}<span style="color: red;"> * </span></label>
                    <InputText id="editGroup.id" type="text" :class="editGroupErrors.id" v-model="editGroupBuffer.id" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editGroup.name">{{ $t("persianName") }}<span style="color: red;"> * </span></label>
                    <InputText id="editGroup.name" type="text" :class="editGroupErrors.name" v-model="editGroupBuffer.name" @keypress="persianInputFilter($event)" @paste="persianInputFilter($event)" />
                    <small>{{ $t("inputPersianFilterText") }}</small>
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editGroup.description">{{ $t("description") }}</label>
                    <Textarea v-model="editGroupBuffer.description" :autoResize="true" rows="3" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editGroup.usersList">{{ $t("addUsers") }}</label>
                    <div v-if="editGroupUsersLoader" class="text-center">
                      <ProgressSpinner />
                    </div>
                    <PickList v-else v-model="editGroupBuffer.usersListBuffer" dataKey="_id" :dir="$store.state.reverseDirection"
                    @move-to-target="editGroupUsersControl($event, 'moveToTarget')" @move-all-to-target="editGroupUsersControl($event, 'moveToTarget')"
                    @move-to-source="editGroupUsersControl($event, 'moveToSource')" @move-all-to-source="editGroupUsersControl($event, 'moveToSource')">
                      <template #sourceheader>
                        {{ $t("nonMemberUsersList") }}
                        <InputText type="text" class="my-2" :placeholder="$t('search')" v-model="editGroupBuffer.nonMemberSearch" @input="editGroupUsersSearch('source')" />
                      </template>
                      <template #targetheader>
                        {{ $t("memberUsersList") }}
                        <InputText type="text" class="my-2" :placeholder="$t('search')" v-model="editGroupBuffer.memberSearch" @input="editGroupUsersSearch('target')" />
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
              <Button :label="$t('confirm')" class="p-button-success mx-1" @click="editGroupCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mx-1" @click="resetState('editGroup')" />
            </div>
          </TabPanel>
        </TabView>
      </div>
    </div>
  </div>
</template>

<script>
import iziToast from "@/assets/scripts/iziToast.min.js"

export default {
  name: "Groups",
  data () {
    return {
      groups: [],
      selectedGroups: [],
      importSelectedGroup: null,
      selectedSampleFile: null,
      sampleFiles: ["CSV", "XLS", "XLSX"],
      createGroupErrors: {
        id: "",
        name: ""
      },
      editGroupErrors: {
        id: "",
        name: ""
      },
      createGroupBuffer: {
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
      editGroupBuffer: {
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
      tabActiveIndex: 0,
      groupToolbarBuffer: "",
      loading: true,
      createGroupFlag: false,
      editGroupFlag: false,
      createGroupLoader: false,
      editGroupLoader: false,
      createGroupUsersLoader: false,
      editGroupUsersLoader: false,
      persianRex: null
    }
  },
  mounted () {
    this.persianRex = require("persian-rex/dist/persian-rex")
    this.groupsRequestMaster("getGroups")
  },
  methods: {
    createGroupUsersSearch (command) {
      if (command === "source") {
        if (this.createGroupBuffer.nonMemberSearch !== "") {
          let buffer = []
          buffer = buffer.concat(this.createGroupBuffer.usersList[0].filter((item) => {
            return this.createGroupBuffer.nonMemberSearch.toLowerCase().split(" ").every(v => item._id.toLowerCase().includes(v))
          }))
          const uniqueBuffer = [...new Set(buffer)]
          this.createGroupBuffer.usersListBuffer[0] = uniqueBuffer
        } else {
          this.createGroupBuffer.usersListBuffer[0] = this.createGroupBuffer.usersList[0]
        }
      } else if (command === "target") {
        if (this.createGroupBuffer.memberSearch !== "") {
          let buffer = []
          buffer = buffer.concat(this.createGroupBuffer.usersList[1].filter((item) => {
            return this.createGroupBuffer.memberSearch.toLowerCase().split(" ").every(v => item._id.toLowerCase().includes(v))
          }))
          const uniqueBuffer = [...new Set(buffer)]
          this.createGroupBuffer.usersListBuffer[1] = uniqueBuffer
        } else {
          this.createGroupBuffer.usersListBuffer[1] = this.createGroupBuffer.usersList[1]
        }
      }
    },
    createGroupUsersControl ($event, command) {
      if (command === "moveToTarget") {
        for (const i in $event.items) {
          this.createGroupBuffer.usersList[1].push($event.items[i])
          const index = this.createGroupBuffer.usersList[0].indexOf($event.items[i])
          this.createGroupBuffer.usersList[0].splice(index, 1)
        }
      } else if (command === "moveToSource") {
        for (const i in $event.items) {
          this.createGroupBuffer.usersList[0].push($event.items[i])
          const index = this.createGroupBuffer.usersList[1].indexOf($event.items[i])
          this.createGroupBuffer.usersList[1].splice(index, 1)
        }
      }
    },
    editGroupUsersSearch (command) {
      if (command === "source") {
        if (this.editGroupBuffer.nonMemberSearch !== "") {
          let buffer = []
          buffer = buffer.concat(this.editGroupBuffer.usersList[0].filter((item) => {
            return this.editGroupBuffer.nonMemberSearch.toLowerCase().split(" ").every(v => item._id.toLowerCase().includes(v))
          }))
          const uniqueBuffer = [...new Set(buffer)]
          this.editGroupBuffer.usersListBuffer[0] = uniqueBuffer
        } else {
          this.editGroupBuffer.usersListBuffer[0] = this.editGroupBuffer.usersList[0]
        }
      } else if (command === "target") {
        if (this.editGroupBuffer.memberSearch !== "") {
          let buffer = []
          buffer = buffer.concat(this.editGroupBuffer.usersList[1].filter((item) => {
            return this.editGroupBuffer.memberSearch.toLowerCase().split(" ").every(v => item._id.toLowerCase().includes(v))
          }))
          const uniqueBuffer = [...new Set(buffer)]
          this.editGroupBuffer.usersListBuffer[1] = uniqueBuffer
        } else {
          this.editGroupBuffer.usersListBuffer[1] = this.editGroupBuffer.usersList[1]
        }
      }
    },
    editGroupUsersControl ($event, command) {
      if (command === "moveToTarget") {
        for (const i in $event.items) {
          this.editGroupBuffer.usersList[1].push($event.items[i])
          const index = this.editGroupBuffer.usersList[0].indexOf($event.items[i])
          this.editGroupBuffer.usersList[0].splice(index, 1)
        }
      } else if (command === "moveToSource") {
        for (const i in $event.items) {
          this.editGroupBuffer.usersList[0].push($event.items[i])
          const index = this.editGroupBuffer.usersList[1].indexOf($event.items[i])
          this.editGroupBuffer.usersList[1].splice(index, 1)
        }
      }
    },
    toggleSampleFile (event) {
      this.$refs.sampleFile.toggle(event)
      this.selectedSampleFile = null
    },
    toggleImportGroup (event) {
      this.$refs.importGroup.toggle(event)
      this.importSelectedGroup = null
    },
    getSampleFile () {
      if (this.selectedSampleFile === "CSV") {
        document.getElementById("getSampleFileCSV").click()
      } else if (this.selectedSampleFile === "XLS") {
        document.getElementById("getSampleFileXLS").click()
      } else if (this.selectedSampleFile === "XLSX") {
        document.getElementById("getSampleFileXLSX").click()
      }
      this.toggleSampleFile()
    },
    groupsRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getGroups") {
        this.loading = true
        this.axios({
          url: "/api/groups",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.groups = res.data.data
            vm.loading = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "getGroup") {
        this.editGroupLoader = true
        this.axios({
          url: "/api/groups",
          method: "GET",
          params: {
            id: vm.editGroupBuffer.id,
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.editGroupBuffer = res.data.data
            vm.editGroupLoader = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editGroupLoader = false
            vm.resetState("editGroup")
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.editGroupLoader = false
          vm.resetState("editGroup")
        })
      } else if (command === "createGroup") {
        this.createGroupBuffer.usersRemoveList = this.createGroupBuffer.usersListBuffer[0].filter(n => !this.createGroupBuffer.usersList[0].includes(n))
        this.createGroupBuffer.usersAddList = this.createGroupBuffer.usersListBuffer[1].filter(n => !this.createGroupBuffer.usersList[1].includes(n))
        this.createGroupLoader = true
        this.axios({
          url: "/api/groups",
          method: "POST",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            id: vm.createGroupBuffer.id,
            name: vm.createGroupBuffer.name,
            description: vm.createGroupBuffer.description
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 201) {
            this.axios({
              url: "/api/users/group/" + vm.createGroupBuffer.id,
              method: "PUT",
              headers: { "Content-Type": "application/json" },
              data: JSON.stringify({
                add: vm.createGroupBuffer.usersAddList,
                remove: vm.createGroupBuffer.usersRemoveList
              }).replace(/\\\\/g, "\\")
            }).then((res1) => {
              if (res1.data.status.code === 200) {
                vm.createGroupLoader = false
                vm.resetState("createGroup")
              } else if (res1.data.status.code === 207) {
                vm.alertPromptMaster(res1.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
                vm.createGroupLoader = false
                vm.resetState("createGroup")
              } else {
                vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                vm.createGroupLoader = false
                vm.resetState("createGroup")
              }
            }).catch(() => {
              vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
              vm.createGroupLoader = false
              vm.resetState("createGroup")
            })
          } else if (res.data.status.code === 302) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createGroupLoader = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createGroupLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.createGroupLoader = false
        })
      } else if (command === "editGroup") {
        this.editGroupBuffer.usersRemoveList = this.editGroupBuffer.usersListBuffer[0].filter(n => !this.editGroupBuffer.usersList[0].includes(n))
        this.editGroupBuffer.usersAddList = this.editGroupBuffer.usersListBuffer[1].filter(n => !this.editGroupBuffer.usersList[1].includes(n))
        this.editGroupLoader = true
        this.axios({
          url: "/api/groups",
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            id: vm.editGroupBuffer.id,
            lang: langCode
          },
          data: JSON.stringify({
            id: vm.editGroupBuffer.id,
            name: vm.editGroupBuffer.name,
            description: vm.editGroupBuffer.description
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            this.axios({
              url: "/api/users/group/" + vm.editGroupBuffer.id,
              method: "PUT",
              headers: { "Content-Type": "application/json" },
              params: {
                lang: langCode
              },
              data: JSON.stringify({
                add: vm.editGroupBuffer.usersAddList,
                remove: vm.editGroupBuffer.usersRemoveList
              }).replace(/\\\\/g, "\\")
            }).then((res1) => {
              if (res1.data.status.code === 200) {
                vm.editGroupLoader = false
                vm.resetState("editUser")
              } else if (res1.data.status.code === 207) {
                vm.alertPromptMaster(res1.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
                vm.editGroupLoader = false
                vm.resetState("editUser")
              } else {
                vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                vm.editGroupLoader = false
                vm.resetState("editUser")
              }
            }).catch(() => {
              vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
              vm.editGroupLoader = false
              vm.resetState("editUser")
            })
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editGroupLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.editGroupLoader = false
        })
      } else if (command === "deleteGroup") {
        const selectedGroupList = [this.groupToolbarBuffer]
        this.loading = true
        this.axios({
          url: "/api/groups",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: selectedGroupList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 204) {
            vm.loading = false
            vm.groupsRequestMaster("getGroups")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "deleteGroups") {
        const selectedGroupsList = []
        for (const x in this.selectedGroups) {
          selectedGroupsList.push(this.selectedGroups[x].id)
        }
        this.loading = true
        this.axios({
          url: "/api/users",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: selectedGroupsList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 204) {
            vm.loading = false
            vm.groupsRequestMaster("getGroups")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "expireGroupPassword") {
        const selectedGroupList = [this.groupToolbarBuffer]
        this.loading = true
        this.axios({
          url: "/api/groups/password/expire",
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: selectedGroupList
          }).replace(/\\\\/g, "\\")
        }).then(() => {
          vm.loading = false
          vm.groupsRequestMaster("getGroups")
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "expireGroupsPasswords") {
        const selectedGroupsList = []
        for (const x in this.selectedGroups) {
          selectedGroupsList.push(this.selectedGroups[x].id)
        }
        this.loading = true
        this.axios({
          url: "/api/users/password/expire",
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: selectedGroupsList
          }).replace(/\\\\/g, "\\")
        }).then(() => {
          vm.loading = false
          vm.groupsRequestMaster("getGroups")
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "getUsersCreateGroup") {
        this.createGroupUsersLoader = true
        this.axios({
          url: "/api/users",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.createGroupBuffer.usersList[0] = res.data.data
            vm.createGroupBuffer.usersListBuffer[0] = res.data.data
            vm.createGroupUsersLoader = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createGroupUsersLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.createGroupUsersLoader = false
        })
      } else if (command === "getUsersEditGroup") {
        this.editGroupUsersLoader = true
        this.axios({
          url: "/api/users",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.editGroupBuffer.usersList = [[], []]
            vm.editGroupBuffer.usersList[0] = res.data.data
            this.axios({
              url: "/api/users/group/" + vm.editGroupBuffer.id,
              method: "GET",
              params: {
                lang: langCode
              }
            }).then((res1) => {
              if (res1.data.status.code === 200) {
                vm.editGroupBuffer.usersListBuffer = [[], []]
                vm.editGroupBuffer.usersList[1] = res1.data.data.userList
                vm.editGroupBuffer.usersListBuffer[1] = res1.data.data.userList
                vm.editGroupBuffer.usersList[0] = vm.editGroupBuffer.usersList[0].filter(a => !vm.editGroupBuffer.usersList[1].map(b => b._id).includes(a._id))
                vm.editGroupBuffer.usersListBuffer[0] = vm.editGroupBuffer.usersList[0]
                vm.editGroupUsersLoader = false
              } else {
                vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                vm.editGroupUsersLoader = false
              }
            }).catch((error) => {
              console.log(error)
              vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
              vm.editGroupUsersLoader = false
            })
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editGroupUsersLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.editGroupUsersLoader = false
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
    importGroups () {
      const vm = this
      const re = /(\.csv|\.xls|\.xlsx)$/i
      const file = document.getElementById("importGroupsInput")
      const fileName = file.value
      if (!re.exec(fileName)) {
        this.alertPromptMaster(this.$t("fileExtensionNotSupported"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        const bodyFormData = new FormData()
        bodyFormData.append("file", file.files[0])
        this.importRepetitiveGroupsList = []
        this.loading = true
        this.axios({
          url: "/api/users/ou/" + vm.importSelectedGroup.id,
          method: "PUT",
          headers: { "Content-Type": "multipart/form-data" },
          data: bodyFormData
        }).then((res) => {
          vm.alertPromptMaster(res.data.status.result, "", "pi-question-circle", "#F0EAAA")
          vm.loading = false
          vm.groupsRequestMaster("getGroups")
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      }
    },
    importGroupsHelper () {
      document.getElementById("importGroupsInput").click()
    },
    createGroup () {
      this.createGroupFlag = true
      this.tabActiveIndex = 1
      this.groupsRequestMaster("getUsersCreateGroup")
    },
    createGroupCheckup () {
      let errorCount = 0
      if (this.createGroupBuffer.id === "") {
        this.createGroupErrors.id = "p-invalid"
        errorCount += 1
      } else {
        this.createGroupErrors._id = ""
      }
      if (this.createGroupBuffer.name === "") {
        this.createGroupErrors.name = "p-invalid"
        errorCount += 1
      } else {
        this.createGroupErrors.name = ""
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.groupsRequestMaster("createGroup")
      }
    },
    editGroup (id) {
      this.editGroupBuffer.id = id
      this.editGroupFlag = true
      if (this.createGroupFlag) {
        this.tabActiveIndex = 2
      } else {
        this.tabActiveIndex = 1
      }
      this.groupsRequestMaster("getGroup")
      this.groupsRequestMaster("getUsersEditGroup")
    },
    editGroupCheckup () {
      let errorCount = 0
      if (this.editGroupBuffer.id === "") {
        this.editGroupErrors.id = "p-invalid"
        errorCount += 1
      } else {
        this.editGroupErrors.id = ""
      }
      if (this.editGroupBuffer.name === "") {
        this.editGroupErrors.name = "p-invalid"
        errorCount += 1
      } else {
        this.editGroupErrors.name = ""
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.groupsRequestMaster("editGroup")
      }
    },
    deleteGroup (id) {
      this.groupToolbarBuffer = id
      this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + String(id), "pi-question-circle", "#F0EAAA", this.groupsRequestMaster, "deleteGroup")
    },
    expireGroupPassword (id) {
      this.groupToolbarBuffer = id
      this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("expirePassword") + " " + String(id), "pi-question-circle", "#F0EAAA", this.groupsRequestMaster, "expireGroupPassword")
    },
    groupsToolbar (action) {
      if (this.selectedGroups.length > 0) {
        if (action === "delete") {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + this.$t("groups"), "pi-question-circle", "#F0EAAA", this.groupsRequestMaster, "deleteGroups")
        } else if (action === "expirePassword") {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("expirePassword") + " " + this.$t("groups"), "pi-question-circle", "#F0EAAA", this.groupsRequestMaster, "expireGroupsPasswords")
        }
      } else {
        this.alertPromptMaster(this.$t("noGroupSelected"), "", "pi-exclamation-triangle", "#FDB5BA")
      }
    },
    resetState (command) {
      if (command === "createGroup") {
        this.tabActiveIndex = 0
        this.createGroupFlag = false
        this.createGroupBuffer = {
          id: "",
          name: "",
          description: "",
          usersCount: null,
          usersList: [[], []]
        }
      } else if (command === "editGroup") {
        this.tabActiveIndex = 0
        this.editGroupFlag = false
        this.editGroupBuffer = {
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
</style>
