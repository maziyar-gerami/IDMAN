<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("users") }}</h3>
        <TabView v-model:activeIndex="tabActiveIndex">
          <TabPanel :header="$t('usersList')">
            <Toolbar>
              <template #start>
                <Button :label="$t('new')" icon="pi pi-plus" class="p-button-success mx-1" @click="createUser()" />
                <Button :label="$t('delete')" icon="pi pi-trash" class="p-button-danger mx-1" @click="usersToolbar('delete')" />
                <i class="pi pi-bars p-toolbar-separator mx-1" ></i>
                <Button icon="pi pi-undo" class="mx-1" v-tooltip.top="$t('resetPassword')" @click="usersToolbar('resetPassword')" />
                <Button icon="pi pi-calendar-times" class="p-button-warning mx-1" v-tooltip.top="$t('expirePassword')" @click="usersToolbar('expirePassword')" />
              </template>
              <template #end>
                <Button icon="pi pi-download" class="mx-1" @click="importUsersHelper()" v-tooltip.top="'Import'" />
                <input id="importUsersInput" type="file" @change="importUsers()" class="hidden" accept=".xlsx"> <!--, .xls, .csv, .ldif-->
                <Button icon="pi pi-upload" class="mx-1" @click="exportUsers()" v-tooltip.top="'Export'" />
                <form method="GET" action="templates/users.xlsx">
                  <Button type="submit" icon="pi pi-info" class="p-button-secondary mx-1" v-tooltip.top="$t('sample')" />
                </form>
              </template>
            </Toolbar>
            <DataTable :value="users" filterDisplay="menu" dataKey="_id" :rows="rowsPerPage" v-model:filters="filters" :loading="loading"
            v-model:selection="selectedUsers" :filters="filters" class="p-datatable-gridlines" :rowHover="true"
            responsiveLayout="scroll" :scrollable="false" scrollHeight="50vh" scrollDirection="vertical" >
              <template #header>
                <div class="flex justify-content-between flex-column sm:flex-row">
                  <div>
                    <div class="p-inputgroup mx-1">
                      <Button icon="pi pi-times" class="p-button-danger" @click="onFilterEvent('sort', '')" v-tooltip.top="$t('removeSort')" />
                      <Dropdown v-model="selectedSortOption" :options="sortOptions" @change="onFilterEvent('sort', $event.value.id)" optionLabel="name" :placeholder="$t('sort')" />
                    </div>
                  </div>
                  <div>
                    <Paginator v-model:rows="rowsPerPage" v-model:totalRecords="totalRecordsCount" @page="onPaginatorEvent($event)" :rowsPerPageOptions="[10,20,50,100,500]"></Paginator>
                  </div>
                  <div>
                    <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="initiateFilters('clearFilters')" />
                  </div>
                </div>
              </template>
              <template #empty>
                <div class="text-right">
                  {{ $t("noUsersFound") }}
                </div>
              </template>
              <template #loading>
                <div class="text-right">
                  {{ $t("loadingUsers") }}
                </div>
              </template>
              <Column selectionMode="multiple" bodyClass="text-center" style="width:5rem;"></Column>
              <Column field="_id" :header="$t('id')" bodyClass="text-center" style="flex: 0 0 8rem">
                <template #body="{data}">
                  {{ data._id }}
                </template>
                <template #filter="{filterModel,filterCallback}">
                  <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback(); onFilterEvent('_id', filterModel.value)" class="p-column-filter" :placeholder="$t('id')"/>
                </template>
                <template #filterapply="{filterModel,filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); onFilterEvent('_id', filterModel.value)" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); onFilterEvent('_id', '')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column field="displayName" :header="$t('persianName')" bodyClass="text-center" style="flex: 0 0 11rem">
                <template #body="{data}">
                  {{ data.displayName }}
                </template>
                <template #filter="{filterModel,filterCallback}">
                  <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback(); onFilterEvent('displayName', filterModel.value)" class="p-column-filter" :placeholder="$t('persianName')"/>
                </template>
                <template #filterapply="{filterModel,filterCallback}">
                    <Button type="button" icon="pi pi-check" @click="filterCallback(); onFilterEvent('displayName', filterModel.value)" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                    <Button type="button" icon="pi pi-times" @click="filterCallback(); onFilterEvent('displayName', '')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column field="mobile" :header="$t('mobile')" bodyClass="text-center" style="flex: 0 0 10rem">
                <template #body="{data}">
                  {{ data.mobile }}
                </template>
                <template #filter="{filterModel,filterCallback}">
                  <InputText type="text" v-model="filterModel.value" @keydown.enter="filterCallback(); onFilterEvent('mobile', filterModel.value)" class="p-column-filter" :placeholder="$t('mobile')"/>
                </template>
                <template #filterapply="{filterModel,filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); onFilterEvent('mobile', filterModel.value)" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); onFilterEvent('mobile', '')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column field="status" :header="$t('status')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  <div v-if="data.status.value === 'enable'">
                    <Tag severity="success" :value="$t('enable')"></Tag>
                  </div>
                  <div v-else-if="data.status.value === 'disable'">
                    <Tag severity="warning" :value="$t('disabled')"></Tag>
                  </div>
                  <div v-else-if="data.status.value === 'lock'">
                    <Tag severity="danger" :value="$t('locked')"></Tag>
                  </div>
                </template>
                <template #filter="{filterModel,filterCallback}">
                  <Dropdown v-model="filterModel.value" @keydown.enter="filterCallback(); onFilterEvent('status', filterModel.value)" :options="editUserStatusOptions" optionLabel="name" :placeholder="$t('status')" />
                </template>
                <template #filterapply="{filterModel,filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); onFilterEvent('status', filterModel.value)" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); onFilterEvent('status', '')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column field="memberOf" :header="$t('groups')" bodyClass="text-center" style="flex: 0 0 13rem">
                <template #body="{data}">
                  <Button v-for="group in data.memberOf" v-bind:key="group" :label="group.name" class="p-button-info p-button-outlined p-disabled p-button-sm m-1 p-1" />
                </template>
                <template #filter="{filterModel,filterCallback}">
                  <Dropdown v-model="filterModel.value" @keydown.enter="filterCallback(); onFilterEvent('groups', filterModel.value)" :options="groups" optionLabel="name" :placeholder="$t('groups')" />
                </template>
                <template #filterapply="{filterModel,filterCallback}">
                  <Button type="button" icon="pi pi-check" @click="filterCallback(); onFilterEvent('groups', filterModel.value)" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                </template>
                <template #filterclear="{filterCallback}">
                  <Button type="button" icon="pi pi-times" @click="filterCallback(); onFilterEvent('groups', '')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                </template>
              </Column>
              <Column bodyStyle="display: flex;" bodyClass="flex justify-content-evenly flex-wrap card-container text-center" style="flex: 0 0 13rem">
                <template #body="{data}">
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="pi pi-pencil" class="p-button-rounded p-button-warning p-button-outlined mx-1" @click="editUser(data._id)" v-tooltip.top="$t('edit')" />
                  </div>
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="pi pi-refresh" class="p-button-rounded p-button-info p-button-outlined mx-1" @click="resetUserPassword(data._id)" v-tooltip.top="$t('resetPassword')" />
                  </div>
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="pi pi-trash" class="p-button-rounded p-button-danger p-button-outlined mx-1" @click="deleteUser(data._id)" v-tooltip.top="$t('delete')" />
                  </div>
                </template>
              </Column>
            </DataTable>
          </TabPanel>
          <TabPanel v-if="createUserFlag" :header="$t('createUser')">
            <div v-if="createUserLoader" class="text-center">
              <ProgressSpinner />
            </div>
            <div v-else>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser._id">{{ $t("id") }}<span style="color: red;"> * </span></label>
                    <InputText id="createUser._id" type="text" :class="createUserErrors._id" v-model="createUserBuffer._id" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser.displayName">{{ $t("persianName") }}<span style="color: red;"> * </span></label>
                    <InputText id="createUser.displayName" type="text" :class="createUserErrors.displayName" v-model="createUserBuffer.displayName" @keypress="persianInputFilter($event)" @paste="persianInputFilter($event)" />
                    <small>{{ $t("inputPersianFilterText") }}</small>
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser.firstName">{{ $t("englishFirstName") }}<span style="color: red;"> * </span></label>
                    <InputText id="createUser.firstName" type="text" :class="createUserErrors.firstName" v-model="createUserBuffer.firstName" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser.lastName">{{ $t("englishLastName") }}<span style="color: red;"> * </span></label>
                    <InputText id="createUser.lastName" type="text" :class="createUserErrors.lastName" v-model="createUserBuffer.lastName" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser.mobile">{{ $t("mobile") }}<span style="color: red;"> * </span></label>
                    <InputText id="createUser.mobile" type="text" :class="createUserErrors.mobile" v-model="createUserBuffer.mobile" />
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser.mail">{{ $t("email") }}<span style="color: red;"> * </span></label>
                    <InputText id="createUser.mail" type="text" :class="createUserErrors.mail" v-model="createUserBuffer.mail" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser.password">{{ $t("password") }}<span style="color: red;"> * </span></label>
                    <Password id="createUser.password" :class="createUserErrors.userPassword" v-model="createUserBuffer.userPassword" :toggleMask="true" autocomplete="off">
                      <template #header>
                          <h6>{{ $t("passwordStrength") }}</h6>
                      </template>
                      <template #footer>
                          <Divider />
                          <p class="mt-3">{{ $t("passwordRequirement") }}</p>
                          <ul class="pl-2 ml-2 mt-0" style="line-height: 1.5">
                              <li>{{ $t("passwordRequirementText1") }}</li>
                              <li>{{ $t("passwordRequirementText2") }}</li>
                              <li>{{ $t("passwordRequirementText3") }}</li>
                              <li>{{ $t("passwordRequirementText4") }}</li>
                          </ul>
                      </template>
                    </Password>
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser.passwordRepeat">{{ $t("passwordRepeat") }}<span style="color: red;"> * </span></label>
                    <Password id="createUser.passwordRepeat" :class="createUserErrors.userPasswordRepeat" v-model="createUserBuffer.userPasswordRepeat" :toggleMask="true" autocomplete="off" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser.employeeNumber">{{ $t("employeeNumber") }}</label>
                    <InputText id="createUser.employeeNumber" type="text" v-model="createUserBuffer.employeeNumber" />
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser.status">{{ $t("status") }}</label>
                    <Dropdown v-model="createUserBuffer.status" :options="createUserStatusOptions" optionLabel="name" />
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid" style="display: grid;">
                    <label for="createUser.unDeletable">{{ $t("unDeletable") }}</label>
                    <ToggleButton id="createUser.unDeletable" v-model="createUserBuffer.unDeletable" onIcon="pi pi-check" offIcon="pi pi-times" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser.memberOf">{{ $t("groups") }}</label>
                    <MultiSelect id="createUser.memberOf" v-model="createUserBuffer.memberOf" :options="groups" optionLabel="name"
                    :placeholder="$t('select')" :filter="true" :emptyMessage="$t('noGroupsFound')" :emptyFilterMessage="$t('noGroupsFound')">
                      <template #value="slotProps">
                        <div class="inline-flex align-items-center py-1 px-2 bg-primary text-primary border-round mr-2" v-for="option of slotProps.value" :key="option.id">
                          <div>{{option.name}}</div>
                        </div>
                        <template v-if="!slotProps.value || slotProps.value.length === 0">
                          <div class="p-1">{{ $t("select") }}</div>
                        </template>
                      </template>
                      <template #option="slotProps">
                        <div class="flex align-items-center">
                          <div>{{slotProps.option.name}}</div>
                        </div>
                      </template>
                    </MultiSelect>
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="createUser.description">{{ $t("description") }}</label>
                    <Textarea v-model="createUserBuffer.description" :autoResize="true" rows="3" />
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="createUserCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('createUser')" />
            </div>
          </TabPanel>
          <TabPanel v-if="editUserFlag" :header="$t('editUser') + '(' + editUserBuffer._id + ')'">
            <div v-if="editUserLoader" class="text-center">
              <ProgressSpinner />
            </div>
            <div v-else>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editUser._id">{{ $t("id") }}<span style="color: red;"> * </span></label>
                    <InputText id="editUser._id" type="text" :class="editUserErrors._id" v-model="editUserBuffer._id" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" disabled />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editUser.displayName">{{ $t("persianName") }}<span style="color: red;"> * </span></label>
                    <InputText id="editUser.displayName" type="text" :class="editUserErrors.displayName" v-model="editUserBuffer.displayName" @keypress="persianInputFilter($event)" @paste="persianInputFilter($event)" />
                    <small>{{ $t("inputPersianFilterText") }}</small>
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editUser.firstName">{{ $t("englishFirstName") }}<span style="color: red;"> * </span></label>
                    <InputText id="editUser.firstName" type="text" :class="editUserErrors.firstName" v-model="editUserBuffer.firstName" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editUser.lastName">{{ $t("englishLastName") }}<span style="color: red;"> * </span></label>
                    <InputText id="editUser.lastName" type="text" :class="editUserErrors.lastName" v-model="editUserBuffer.lastName" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                    <small>{{ $t("inputEnglishFilterText") }}</small>
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editUser.mobile">{{ $t("mobile") }}<span style="color: red;"> * </span></label>
                    <InputText id="editUser.mobile" type="text" :class="editUserErrors.mobile" v-model="editUserBuffer.mobile" />
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editUser.mail">{{ $t("email") }}<span style="color: red;"> * </span></label>
                    <InputText id="editUser.mail" type="text" :class="editUserErrors.mail" v-model="editUserBuffer.mail" />
                  </div>
                </div>
              </div>
              <Checkbox id="editUser.password.checkbox" v-model="editUserPasswordFlag" :binary="true" />
              <label for="editUser.password.checkbox" class="mx-2">{{ $t("editPassword") }}</label>
              <BlockUI :blocked="!editUserPasswordFlag" class="p-2 mt-2 mb-5">
                <div class="formgrid grid">
                  <div class="field col">
                    <div class="field p-fluid">
                      <label for="editUser.password">{{ $t("password") }}<span style="color: red;"> * </span></label>
                      <Password id="editUser.password" :class="editUserErrors.userPassword" v-model="editUserBuffer.userPassword" :toggleMask="true" autocomplete="off">
                        <template #header>
                            <h6>{{ $t("passwordStrength") }}</h6>
                        </template>
                        <template #footer>
                            <Divider />
                            <p class="mt-3">{{ $t("passwordRequirement") }}</p>
                            <ul class="pl-2 ml-2 mt-0" style="line-height: 1.5">
                                <li>{{ $t("passwordRequirementText1") }}</li>
                                <li>{{ $t("passwordRequirementText2") }}</li>
                                <li>{{ $t("passwordRequirementText3") }}</li>
                                <li>{{ $t("passwordRequirementText4") }}</li>
                            </ul>
                        </template>
                      </Password>
                    </div>
                  </div>
                  <div class="field col">
                    <div class="field p-fluid">
                      <label for="editUser.passwordRepeat">{{ $t("passwordRepeat") }}<span style="color: red;"> * </span></label>
                      <Password id="editUser.passwordRepeat" :class="editUserErrors.userPasswordRepeat" v-model="editUserBuffer.userPasswordRepeat" :toggleMask="true" autocomplete="off" />
                    </div>
                  </div>
                </div>
              </BlockUI>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editUser.employeeNumber">{{ $t("employeeNumber") }}</label>
                    <InputText id="editUser.employeeNumber" type="text" v-model="editUserBuffer.employeeNumber" />
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editUser.status">{{ $t("status") }}</label>
                    <Dropdown v-model="editUserBuffer.status" :options="editUserStatusOptions" optionLabel="name" />
                  </div>
                </div>
                <div class="field col">
                  <div class="field p-fluid" style="display: grid;">
                    <label for="editUser.unDeletable">{{ $t("unDeletable") }}</label>
                    <ToggleButton id="editUser.unDeletable" v-model="editUserBuffer.unDeletable" onIcon="pi pi-check" offIcon="pi pi-times" />
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editUser.memberOf">{{ $t("groups") }}</label>
                    <MultiSelect id="editUser.memberOf" v-model="editUserBuffer.memberOf" :options="groups" optionLabel="name"
                    :placeholder="$t('select')" :filter="true" :emptyMessage="$t('noGroupsFound')" :emptyFilterMessage="$t('noGroupsFound')">
                      <template #value="slotProps">
                        <div class="inline-flex align-items-center py-1 px-2 bg-primary text-primary border-round mr-2" v-for="option of slotProps.value" :key="option.id">
                          <div>{{option.name}}</div>
                        </div>
                        <template v-if="!slotProps.value || slotProps.value.length === 0">
                          <div class="p-1">{{ $t("select") }}</div>
                        </template>
                      </template>
                      <template #option="slotProps">
                        <div class="flex align-items-center">
                          <div>{{slotProps.option.name}}</div>
                        </div>
                      </template>
                    </MultiSelect>
                  </div>
                </div>
              </div>
              <div class="formgrid grid">
                <div class="field col">
                  <div class="field p-fluid">
                    <label for="editUser.description">{{ $t("description") }}</label>
                    <Textarea v-model="editUserBuffer.description" :autoResize="true" rows="3" />
                  </div>
                </div>
              </div>
              <Button :label="$t('confirm')" class="p-button-success mx-1" @click="editUserCheckup()" />
              <Button :label="$t('back')" class="p-button-danger mx-1" @click="resetState('editUser')" />
            </div>
          </TabPanel>
        </TabView>
      </div>
    </div>
  </div>
</template>

<script>
import { FilterMatchMode } from "primevue/api"
import iziToast from "@/assets/scripts/iziToast.min.js"

export default {
  name: "Users",
  data () {
    return {
      users: [],
      groups: [],
      selectedUsers: [],
      importRepetitiveUsersList: [],
      sortOptions: [
        {
          id: "uid_m2M",
          name: this.$t("idAscending")
        },
        {
          id: "uid_M2m",
          name: this.$t("idDescending")
        },
        {
          id: "displayName_m2M",
          name: this.$t("persianNameAscending")
        },
        {
          id: "displayName_M2m",
          name: this.$t("persianNameDescending")
        }
      ],
      createUserStatusOptions: [
        {
          value: "enable",
          name: this.$t("enable")
        },
        {
          value: "disable",
          name: this.$t("disabled")
        }
      ],
      editUserStatusOptions: [
        {
          value: "enable",
          name: this.$t("enable")
        },
        {
          value: "disable",
          name: this.$t("disabled")
        },
        {
          value: "lock",
          name: this.$t("locked")
        }
      ],
      createUserErrors: {
        _id: "",
        displayName: "",
        firstName: "",
        lastName: "",
        mobile: "",
        mail: "",
        userPassword: "",
        userPasswordRepeat: ""
      },
      editUserErrors: {
        _id: "",
        displayName: "",
        firstName: "",
        lastName: "",
        mobile: "",
        mail: "",
        userPassword: "",
        userPasswordRepeat: ""
      },
      createUserBuffer: {
        _id: "",
        displayName: "",
        firstName: "",
        lastName: "",
        mobile: "",
        mail: "",
        employeeNumber: "",
        status: { value: "enable", name: this.$t("enable") },
        unDeletable: false,
        memberOf: [],
        description: "",
        userPassword: "",
        userPasswordRepeat: ""
      },
      editUserBuffer: {
        _id: "",
        displayName: "",
        firstName: "",
        lastName: "",
        mobile: "",
        mail: "",
        employeeNumber: "",
        status: { value: "enable", name: this.$t("enable") },
        unDeletable: false,
        memberOf: [],
        description: "",
        userPassword: "",
        userPasswordRepeat: ""
      },
      selectedSortOption: {},
      rowsPerPage: 20,
      newPageNumber: 1,
      totalRecordsCount: 20,
      tabActiveIndex: 0,
      userToolbarBuffer: "",
      loading: false,
      createUserFlag: false,
      editUserFlag: false,
      editUserPasswordFlag: false,
      createUserLoader: false,
      editUserLoader: false,
      persianRex: null,
      filters: null,
      filterValues: null
    }
  },
  mounted () {
    this.persianRex = require("persian-rex/dist/persian-rex")
    this.initiateFilters()
    this.usersRequestMaster("initiateGroups")
  },
  methods: {
    initiateFilters (from) {
      this.filters = {
        _id: { value: null, matchMode: FilterMatchMode.CONTAINS },
        displayName: { value: null, matchMode: FilterMatchMode.CONTAINS },
        mobile: { value: null, matchMode: FilterMatchMode.CONTAINS },
        status: { value: null, matchMode: FilterMatchMode.CONTAINS },
        memberOf: { value: null, matchMode: FilterMatchMode.CONTAINS }
      }
      this.filterValues = {
        _id: "",
        displayName: "",
        mobile: "",
        status: { value: "", name: "" },
        groups: { id: "", name: "" },
        sort: ""
      }
      if (from === "clearFilters") {
        this.onFilterEvent("")
      }
    },
    onPaginatorEvent (event) {
      this.newPageNumber = event.page + 1
      this.rowsPerPage = event.rows
      this.onFilterEvent("")
    },
    onFilterEvent (filterName, filterValue) {
      if (filterName !== "") {
        this.filterValues[filterName] = filterValue
      }
      if (filterName === "sort" && filterValue === "") {
        this.selectedSortOption = {}
      }
      this.usersRequestMaster("getUsers")
    },
    usersRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getUsers") {
        this.loading = true
        this.axios({
          url: "/api/users/" + String(vm.newPageNumber) + "/" + String(vm.rowsPerPage),
          method: "GET",
          params: {
            searchUid: vm.filterValues._id,
            searchDisplayName: vm.filterValues.displayName,
            mobile: vm.filterValues.mobile,
            userStatus: vm.filterValues.status.value,
            groupFilter: vm.filterValues.groups.id,
            sortType: vm.filterValues.sort,
            lang: langCode
          }
        }).then((res) => {
          vm.totalRecordsCount = res.data.data.size
          vm.users = res.data.data.userList
          for (const x in vm.users) {
            if (vm.users[x].status === "enable") {
              vm.users[x].status = { value: "enable", name: this.$t("enable") }
            } else if (vm.users[x].status === "disable") {
              vm.users[x].status = { value: "disable", name: this.$t("disabled") }
            } else if (vm.users[x].status === "lock") {
              vm.users[x].status = { value: "lock", name: this.$t("locked") }
            } else {
              vm.users[x].status = { value: "", name: "" }
            }
            for (const y in vm.users[x].memberOf) {
              for (const z in vm.groups) {
                if (vm.groups[z].id === vm.users[x].memberOf[y]) {
                  vm.users[x].memberOf[y] = vm.groups[z]
                  break
                }
              }
            }
          }
          vm.loading = false
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "getUser") {
        this.editUserLoader = true
        this.axios({
          url: "/api/users/u/" + this.editUserBuffer._id,
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          vm.editUserBuffer = res.data.data
          if (vm.editUserBuffer.status === "enable") {
            vm.editUserBuffer.status = { value: "enable", name: this.$t("enable") }
          } else if (vm.editUserBuffer.status === "disable") {
            vm.editUserBuffer.status = { value: "disable", name: this.$t("disabled") }
          } else if (vm.editUserBuffer.status === "lock") {
            vm.editUserBuffer.status = { value: "lock", name: this.$t("locked") }
          }
          for (const i in vm.editUserBuffer.memberOf) {
            for (const j in vm.groups) {
              if (vm.groups[j].id === vm.editUserBuffer.memberOf[i]) {
                vm.editUserBuffer.memberOf[i] = vm.groups[j]
                break
              }
            }
          }
          vm.editUserLoader = false
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.editUserLoader = false
          vm.resetState("editUser")
        })
      } else if (command === "createUser") {
        const memberOf = []
        for (let i = 0; i < this.createUserBuffer.memberOf.length; ++i) {
          memberOf.push(this.createUserBuffer.memberOf[i].id)
        }
        this.createUserLoader = true
        this.axios({
          url: "/api/users",
          method: "POST",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            _id: vm.createUserBuffer._id,
            displayName: vm.createUserBuffer.displayName,
            firstName: vm.createUserBuffer.firstName,
            lastName: vm.createUserBuffer.lastName,
            mobile: vm.createUserBuffer.mobile,
            mail: vm.createUserBuffer.mail,
            employeeNumber: vm.createUserBuffer.employeeNumber,
            status: vm.createUserBuffer.status.value,
            unDeletable: vm.createUserBuffer.unDeletable,
            memberOf: memberOf,
            description: vm.createUserBuffer.description,
            userPassword: vm.createUserBuffer.userPassword
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 302) {
            vm.alertPromptMaster(res.data.status.result, "", "pi-exclamation-triangle", "#FDB5BA")
          } else {
            vm.resetState("createUser")
          }
          vm.createUserLoader = false
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.createUserLoader = false
        })
      } else if (command === "editUser") {
        const memberOf = []
        for (let i = 0; i < this.editUserBuffer.memberOf.length; ++i) {
          memberOf.push(this.editUserBuffer.memberOf[i].id)
        }
        this.editUserLoader = true
        if (this.editUserPasswordFlag) {
          this.axios({
            url: "/api/users",
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            params: {
              lang: langCode
            },
            data: JSON.stringify({
              _id: vm.editUserBuffer._id,
              displayName: vm.editUserBuffer.displayName,
              firstName: vm.editUserBuffer.firstName,
              lastName: vm.editUserBuffer.lastName,
              mobile: vm.editUserBuffer.mobile,
              mail: vm.editUserBuffer.mail,
              employeeNumber: vm.editUserBuffer.employeeNumber,
              status: vm.editUserBuffer.status.value,
              unDeletable: vm.editUserBuffer.unDeletable,
              memberOf: memberOf,
              description: vm.editUserBuffer.description,
              userPassword: vm.editUserBuffer.userPassword
            }).replace(/\\\\/g, "\\")
          }).then(() => {
            vm.editUserLoader = false
            vm.resetState("editUser")
          }).catch((error) => {
            if (typeof error.response !== "undefined") {
              if (error.response.status === 302) {
                vm.alertPromptMaster(vm.$t("duplicatePasswordsError"), "", "pi-exclamation-triangle", "#FDB5BA")
              } else {
                vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
              }
            } else {
              vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            }
            vm.editUserLoader = false
          })
        } else {
          this.axios({
            url: "/api/users",
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            params: {
              lang: langCode
            },
            data: JSON.stringify({
              _id: vm.editUserBuffer._id,
              displayName: vm.editUserBuffer.displayName,
              firstName: vm.editUserBuffer.firstName,
              lastName: vm.editUserBuffer.lastName,
              mobile: vm.editUserBuffer.mobile,
              mail: vm.editUserBuffer.mail,
              employeeNumber: vm.editUserBuffer.employeeNumber,
              status: vm.editUserBuffer.status.value,
              unDeletable: vm.editUserBuffer.unDeletable,
              memberOf: memberOf,
              description: vm.editUserBuffer.description
            }).replace(/\\\\/g, "\\")
          }).then(() => {
            vm.editUserLoader = false
            vm.resetState("editUser")
          }).catch((error) => {
            if (typeof error.response !== "undefined") {
              if (error.response.status === 302) {
                vm.alertPromptMaster(vm.$t("duplicatePasswordsError"), "", "pi-exclamation-triangle", "#FDB5BA")
              } else {
                vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
              }
            } else {
              vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            }
            vm.editUserLoader = false
          })
        }
      } else if (command === "deleteUser") {
        const selectedUserList = [this.userToolbarBuffer]
        this.loading = true
        this.axios({
          url: "/api/users",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: selectedUserList
          }).replace(/\\\\/g, "\\")
        }).then(() => {
          vm.loading = false
          vm.usersRequestMaster("getUsers")
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "deleteUsers") {
        const selectedUsersList = []
        for (const x in this.selectedUsers) {
          selectedUsersList.push(this.selectedUsers[x]._id)
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
            names: selectedUsersList
          }).replace(/\\\\/g, "\\")
        }).then(() => {
          vm.loading = false
          vm.usersRequestMaster("getUsers")
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "resetUserPassword") {
        const selectedUserList = [this.userToolbarBuffer]
        this.loading = true
        this.axios({
          url: "/api/users/sendMail",
          method: "POST",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: selectedUserList
          }).replace(/\\\\/g, "\\")
        }).then(() => {
          vm.loading = false
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "resetUsersPasswords") {
        const selectedUsersList = []
        for (const x in this.selectedUsers) {
          selectedUsersList.push(this.selectedUsers[x]._id)
        }
        this.loading = true
        this.axios({
          url: "/api/users/sendMail",
          method: "POST",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: selectedUsersList
          }).replace(/\\\\/g, "\\")
        }).then(() => {
          vm.loading = false
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "expireUsersPasswords") {
        const selectedUsersList = []
        for (const x in this.selectedUsers) {
          selectedUsersList.push(this.selectedUsers[x]._id)
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
            names: selectedUsersList
          }).replace(/\\\\/g, "\\")
        }).then(() => {
          vm.loading = false
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      } else if (command === "initiateGroups") {
        this.loading = true
        this.axios({
          url: "/api/groups",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          vm.groups = res.data.data
          vm.loading = false
          vm.usersRequestMaster("getUsers")
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
    exportUsers () {
      const vm = this
      this.loading = true
      this.axios({
        url: "/api/users/export",
        method: "GET",
        responseType: "blob"
      }).then((res) => {
        const url = window.URL.createObjectURL(new Blob([res.data]))
        const link = document.createElement("a")
        link.href = url
        link.setAttribute("download", "users.xls")
        document.body.appendChild(link)
        link.click()
        vm.loading = false
      }).catch(() => {
        vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
        vm.loading = false
      })
    },
    importUsers () {
      const vm = this
      const re = /(\.xlsx)$/i
      const file = document.getElementById("importUsersInput")
      const fileName = file.value
      if (!re.exec(fileName)) {
        this.alertPromptMaster(this.$t("fileExtensionNotSupported"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        const bodyFormData = new FormData()
        bodyFormData.append("file", file.files[0])
        this.importRepetitiveUsersList = []
        this.loading = true
        this.axios({
          url: "/api/users/import",
          method: "POST",
          headers: { "Content-Type": "multipart/form-data" },
          data: bodyFormData
        }).then((res) => {
          if (res.data.status.code === 302) {
            vm.alertPromptMaster(
              vm.$t("usersImportText1") + String(res.data.data.nSuccessful) + vm.$t("usersImportText2") + String(res.data.data.nUnSuccessful) + vm.$t("usersImportText3"),
              "",
              "pi-question-circle",
              "#F0EAAA"
            )
            if (res.data.data.invalidGroups.length !== 0) {
              let importGroupErrorUsersListText = ""
              for (let i = 0; i < res.data.data.invalidGroups.length - 1; ++i) {
                importGroupErrorUsersListText = importGroupErrorUsersListText + res.data.data.invalidGroups[i]._id + ", "
              }
              importGroupErrorUsersListText = importGroupErrorUsersListText + res.data.data.invalidGroups[res.data.data.invalidGroups.length - 1]._id
              vm.alertPromptMaster(vm.$t("usersImportGroupErrorText"), importGroupErrorUsersListText, "pi-question-circle", "#F0EAAA")
            }
            if (res.data.data.repetitiveUsers.length !== 0) {
              let importRepetitiveUsersListText = ""
              for (let i = 0; i < res.data.data.repetitiveUsers.length - 1; ++i) {
                importRepetitiveUsersListText = importRepetitiveUsersListText + res.data.data.repetitiveUsers[i].new._id + ", "
                vm.importRepetitiveUsersList.push(res.data.data.repetitiveUsers[i].new)
              }
              importRepetitiveUsersListText = importRepetitiveUsersListText + res.data.data.repetitiveUsers[res.data.data.repetitiveUsers.length - 1].new._id
              vm.importRepetitiveUsersList.push(res.data.data.repetitiveUsers[res.data.data.repetitiveUsers.length - 1].new)
              vm.confirmPromptMaster(vm.$t("usersImportRepetitiveErrorText"), importRepetitiveUsersListText, "pi-question-circle", "#F0EAAA", vm.importRepetitiveUsers)
            }
          } else {
            vm.alertPromptMaster(
              vm.$t("usersImportText1") + String(res.data.data.nSuccessful) + vm.$t("usersImportText2") + String(res.data.data.nUnSuccessful) + vm.$t("usersImportText3"),
              "",
              "pi-question-circle",
              "#F0EAAA"
            )
          }
          vm.loading = false
          vm.usersRequestMaster("getUsers")
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        })
      }
    },
    importUsersHelper () {
      document.getElementById("importUsersInput").click()
    },
    importRepetitiveUsers () {
      const vm = this
      this.loading = true
      this.axios({
        url: "/api/users/import/massUpdate",
        method: "PUT",
        headers: { "Content-Type": "application/json" },
        data: vm.importRepetitiveUsersList
      }).then((res) => {
        vm.alertPromptMaster(
          vm.$t("usersImportOverrideText1") + String(res.data.data.nSuccessful) + vm.$t("usersImportOverrideText2") + String(res.data.data.nUnSuccessful) + vm.$t("usersImportOverrideText3"),
          "",
          "pi-question-circle",
          "#F0EAAA"
        )
        vm.loading = false
        vm.usersRequestMaster("getUsers")
      }).catch(() => {
        vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
        vm.loading = false
      })
    },
    createUser () {
      this.createUserFlag = true
      this.tabActiveIndex = 1
    },
    createUserCheckup () {
      let errorCount = 0
      const emailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      const mobileRegex = /^(\+98|0)?9\d{9}$/
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})/
      if (this.createUserBuffer._id === "") {
        this.createUserErrors._id = "p-invalid"
        errorCount += 1
      } else {
        this.createUserErrors._id = ""
      }
      if (this.createUserBuffer.displayName === "") {
        this.createUserErrors.displayName = "p-invalid"
        errorCount += 1
      } else {
        this.createUserErrors.displayName = ""
      }
      if (this.createUserBuffer.firstName === "") {
        this.createUserErrors.firstName = "p-invalid"
        errorCount += 1
      } else {
        this.createUserErrors.firstName = ""
      }
      if (this.createUserBuffer.lastName === "") {
        this.createUserErrors.lastName = "p-invalid"
        errorCount += 1
      } else {
        this.createUserErrors.lastName = ""
      }
      if (this.createUserBuffer.mobile === "") {
        this.createUserErrors.mobile = "p-invalid"
        errorCount += 1
      } else {
        if (!mobileRegex.test(Number(this.createUserBuffer.mobile))) {
          this.createUserErrors.mobile = "p-invalid"
          errorCount += 1
        } else {
          this.createUserErrors.mobile = ""
        }
      }
      if (this.createUserBuffer.mail === "") {
        this.createUserErrors.mail = "p-invalid"
        errorCount += 1
      } else {
        if (!emailRegex.test(this.createUserBuffer.mail)) {
          this.createUserErrors.mail = "p-invalid"
          errorCount += 1
        } else {
          this.createUserErrors.mail = ""
        }
      }
      if (this.createUserBuffer.userPassword === "") {
        this.createUserErrors.userPassword = "p-invalid"
        errorCount += 1
      } else {
        if (this.createUserBuffer.userPasswordRepeat === "") {
          this.createUserErrors.userPasswordRepeat = "p-invalid"
          errorCount += 1
        } else {
          if (this.createUserBuffer.userPassword !== this.createUserBuffer.userPasswordRepeat) {
            this.createUserErrors.userPassword = "p-invalid"
            this.createUserErrors.userPasswordRepeat = "p-invalid"
            errorCount += 1
          } else {
            if (!passwordRegex.test(this.createUserBuffer.userPassword)) {
              this.createUserErrors.userPassword = "p-invalid"
              errorCount += 1
            } else {
              this.createUserErrors.userPassword = ""
              this.createUserErrors.userPasswordRepeat = ""
            }
          }
        }
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.usersRequestMaster("createUser")
      }
    },
    editUser (id) {
      this.editUserBuffer._id = id
      this.editUserFlag = true
      if (this.createUserFlag) {
        this.tabActiveIndex = 2
      } else {
        this.tabActiveIndex = 1
      }
      this.usersRequestMaster("getUser")
    },
    editUserCheckup () {
      let errorCount = 0
      const emailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      const mobileRegex = /^(\+98|0)?9\d{9}$/
      const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.{8,})/
      if (this.editUserBuffer._id === "") {
        this.editUserErrors._id = "p-invalid"
        errorCount += 1
      } else {
        this.editUserErrors._id = ""
      }
      if (this.editUserBuffer.displayName === "") {
        this.editUserErrors.displayName = "p-invalid"
        errorCount += 1
      } else {
        this.editUserErrors.displayName = ""
      }
      if (this.editUserBuffer.firstName === "") {
        this.editUserErrors.firstName = "p-invalid"
        errorCount += 1
      } else {
        this.editUserErrors.firstName = ""
      }
      if (this.editUserBuffer.lastName === "") {
        this.editUserErrors.lastName = "p-invalid"
        errorCount += 1
      } else {
        this.editUserErrors.lastName = ""
      }
      if (this.editUserBuffer.mobile === "") {
        this.editUserErrors.mobile = "p-invalid"
        errorCount += 1
      } else {
        if (!mobileRegex.test(Number(this.editUserBuffer.mobile))) {
          this.editUserErrors.mobile = "p-invalid"
          errorCount += 1
        } else {
          this.editUserErrors.mobile = ""
        }
      }
      if (this.editUserBuffer.mail === "") {
        this.editUserErrors.mail = "p-invalid"
        errorCount += 1
      } else {
        if (!emailRegex.test(this.editUserBuffer.mail)) {
          this.editUserErrors.mail = "p-invalid"
          errorCount += 1
        } else {
          this.editUserErrors.mail = ""
        }
      }
      if (!this.editUserPasswordFlag) {
        this.editUserErrors.userPassword = ""
        this.editUserErrors.userPasswordRepeat = ""
      } else {
        if (this.editUserBuffer.userPassword === "") {
          this.editUserErrors.userPassword = "p-invalid"
          errorCount += 1
        } else {
          if (this.editUserBuffer.userPasswordRepeat === "") {
            this.editUserErrors.userPasswordRepeat = "p-invalid"
            errorCount += 1
          } else {
            if (this.editUserBuffer.userPassword !== this.editUserBuffer.userPasswordRepeat) {
              this.editUserErrors.userPassword = "p-invalid"
              this.editUserErrors.userPasswordRepeat = "p-invalid"
              errorCount += 1
            } else {
              if (!passwordRegex.test(this.editUserBuffer.userPassword)) {
                this.editUserErrors.userPassword = "p-invalid"
                errorCount += 1
              } else {
                this.editUserErrors.userPassword = ""
                this.editUserErrors.userPasswordRepeat = ""
              }
            }
          }
        }
      }
      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.usersRequestMaster("editUser")
      }
    },
    deleteUser (id) {
      this.userToolbarBuffer = id
      this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + String(id), "pi-question-circle", "#F0EAAA", this.usersRequestMaster, "deleteUser")
    },
    resetUserPassword (id) {
      this.userToolbarBuffer = id
      this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("resetPassword") + " " + String(id), "pi-question-circle", "#F0EAAA", this.usersRequestMaster, "resetUserPassword")
    },
    usersToolbar (action) {
      if (this.selectedUsers.length > 0) {
        if (action === "delete") {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + this.$t("users"), "pi-question-circle", "#F0EAAA", this.usersRequestMaster, "deleteUsers")
        } else if (action === "resetPassword") {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("resetPassword") + " " + this.$t("users"), "pi-question-circle", "#F0EAAA", this.usersRequestMaster, "resetUsersPasswords")
        } else if (action === "expirePassword") {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("expirePassword") + " " + this.$t("users"), "pi-question-circle", "#F0EAAA", this.usersRequestMaster, "expireUsersPasswords")
        }
      } else {
        this.alertPromptMaster(this.$t("noUserSelected"), "", "pi-exclamation-triangle", "#FDB5BA")
      }
    },
    resetState (command) {
      if (command === "createUser") {
        this.tabActiveIndex = 0
        this.createUserFlag = false
        this.createUserBuffer = {
          _id: "",
          displayName: "",
          firstName: "",
          lastName: "",
          mobile: "",
          mail: "",
          employeeNumber: "",
          status: { value: "enable", name: this.$t("enable") },
          unDeletable: false,
          memberOf: [],
          description: "",
          userPassword: ""
        }
      } else if (command === "editUser") {
        this.tabActiveIndex = 0
        this.editUserFlag = false
        this.editUserBuffer = {
          _id: "",
          displayName: "",
          firstName: "",
          lastName: "",
          mobile: "",
          mail: "",
          employeeNumber: "",
          status: { value: "enable", name: this.$t("enable") },
          unDeletable: false,
          memberOf: [],
          description: "",
          userPassword: ""
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
.p-column-filter-menu {
  margin: 0;
}
</style>
