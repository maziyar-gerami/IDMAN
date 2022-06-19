<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("services") }}</h3>
        <TabView v-model:activeIndex="tabActiveIndexList">
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
              <Column field="url" :header="$t('url')" bodyClass="text-center" bodyStyle="direction: ltr;" style="flex: 0 0 15rem">
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
                    <Button icon="bx bx-fw bxs-pie-chart-alt-2" class="p-button-rounded p-button-info p-button-outlined mx-1" @click="toggleExportAccessReports($event, data._id)" v-tooltip.top="$t('serviceAccessReport')" />
                    <OverlayPanel :ref="'exportAccessReports' + data._id">
                      <div class="p-inputgroup mx-1">
                        <Button label="PDF" @click="exportAccessReports(data._id, 'pdf')" />
                        <Button label="XLSX" @click="exportAccessReports(data._id, 'xlsx')" />
                      </div>
                    </OverlayPanel>
                  </div>
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="bx bxs-trash bx-sm" class="p-button-rounded p-button-danger p-button-outlined mx-1" @click="deleteService(data._id, data.name)" v-tooltip.top="$t('delete')" />
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
                  <h4>{{ $t("serviceInformation") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col-6">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.serviceType">{{ $t("serviceType") }}</label>
                        <Dropdown id="createServiceBuffer.serviceType" :options="['CAS','SAML','Oauth2']" v-model="createServiceBuffer.serviceType" />
                      </div>
                    </div>
                    <div class="field col-3">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.accessStrategy.enabled" class="flex">{{ $t("enableService") }}</label>
                        <InputSwitch id="createServiceBuffer.accessStrategy.enabled" v-model="createServiceBuffer.accessStrategy.enabled" />
                      </div>
                    </div>
                    <div class="field col-3">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.accessStrategy.ssoEnabled" class="flex">{{ $t("allowSSO") }}</label>
                        <InputSwitch id="createServiceBuffer.accessStrategy.ssoEnabled" v-model="createServiceBuffer.accessStrategy.ssoEnabled" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.name">{{ $t("serviceName") }}<span style="color: red;"> * </span></label>
                        <InputText id="createServiceBuffer.name" type="text" :class="createServiceErrors.name" v-model="createServiceBuffer.name" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                        <small>{{ $t("inputEnglishFilterText") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.description">{{ $t("serviceFarsiName") }}<span style="color: red;"> * </span></label>
                        <InputText id="createServiceBuffer.description" type="text" :class="createServiceErrors.description" v-model="createServiceBuffer.description" @keypress="persianInputFilter($event)" @paste="persianInputFilter($event)" />
                        <small>{{ $t("inputPersianFilterText") }}</small>
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.serviceId">{{ $t("serviceID") }}<span style="color: red;"> * </span></label>
                        <InputText id="createServiceBuffer.serviceId" type="text" :class="createServiceErrors.serviceId" v-model="createServiceBuffer.serviceId" dir="ltr" />
                        <small>{{ $t("serviceURLText1") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.extraInfo.url">{{ $t("serviceURL") }}<span style="color: red;"> * </span></label>
                        <InputText id="createServiceBuffer.extraInfo.url" type="text" :class="createServiceErrors.extraInfo.url" v-model="createServiceBuffer.extraInfo.url" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                  </div>
                  <div v-if="createServiceBuffer.serviceType === 'SAML'" class="formgrid grid">
                    <div class="field col-2">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.metadataLocation" class="flex">Metadata Location</label>
                        <div class="field-radiobutton">
                          <RadioButton id="metadataLocationAddress" name="metadataLocation" value="address" v-model="createServiceBuffer.metadataLocationOption" />
                          <label for="metadataLocationAddress" class="mx-2">{{ $t("url") }}</label>
                        </div>
                        <div class="field-radiobutton">
                          <RadioButton id="metadataLocationFile" name="metadataLocation" value="file" v-model="createServiceBuffer.metadataLocationOption" />
                          <label for="metadataLocationFile" class="mx-2">{{ $t("file") }}</label>
                        </div>
                      </div>
                    </div>
                    <div class="field col-6">
                      <div v-if="createServiceBuffer.metadataLocationOption === 'address'" class="field p-fluid">
                        <InputText id="createServiceBuffer.metadataLocation" type="text" :class="createServiceErrors.metadataLocation" v-model="createServiceBuffer.metadataLocation" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                      <div v-else-if="createServiceBuffer.metadataLocationOption === 'file'" class="field p-fluid">
                        <FileUpload id="createServiceBuffer.metadataLocation" mode="basic" name="file" :chooseLabel="$t('selectFile')"
                        @select="fileUploadHelper($event, 'create', 'metadata')" :class="createServiceErrors.metadataLocation" accept=".xml" />
                      </div>
                    </div>
                  </div>
                  <div v-if="createServiceBuffer.serviceType === 'Oauth2'" class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.clientId">Client Id<span style="color: red;"> * </span></label>
                        <InputText id="createServiceBuffer.clientId" type="text" :class="createServiceErrors.clientId" v-model="createServiceBuffer.clientId" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.clientSecret">Client Secret<span style="color: red;"> * </span></label>
                        <div class="p-inputgroup">
                          <InputText id="createServiceBuffer.clientSecret" v-model="createServiceBuffer.clientSecret" :class="createServiceErrors.clientSecret" />
                          <Button @click="generateSecret('create')">{{ $t("generateSecret") }}</Button>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div v-if="createServiceBuffer.serviceType === 'Oauth2'" class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.supportedGrantTypes">Supported Grant Types<span style="color: red;"> * </span></label>
                        <MultiSelect id="createServiceBuffer.supportedGrantTypes" v-model="createServiceBuffer.supportedGrantTypes[1]" :class="createServiceErrors.supportedGrantTypes"
                        :options="['none', 'authorization_code', 'password', 'client_credentials', 'refresh_token', 'uma_ticket']" :placeholder="$t('select')">
                          <template #value="slotProps">
                            <div class="inline-flex align-items-center py-1 px-2 bg-primary text-primary border-round mr-2" v-for="option of slotProps.value" :key="option">
                              <div>{{option}}</div>
                            </div>
                            <template v-if="!slotProps.value || slotProps.value.length === 0">
                              <div class="p-1">{{ $t("select") }}</div>
                            </template>
                          </template>
                          <template #option="slotProps">
                            <div class="flex align-items-center">
                              <div>{{slotProps.option}}</div>
                            </div>
                          </template>
                        </MultiSelect>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.supportedResponseTypes">Supported Response Types<span style="color: red;"> * </span></label>
                        <MultiSelect id="createServiceBuffer.supportedResponseTypes" v-model="createServiceBuffer.supportedResponseTypes[1]" :class="createServiceErrors.supportedResponseTypes"
                        :options="['none', 'code', 'token', 'device_code', 'idtoken_token', 'id_token']" :placeholder="$t('select')">
                          <template #value="slotProps">
                            <div class="inline-flex align-items-center py-1 px-2 bg-primary text-primary border-round mr-2" v-for="option of slotProps.value" :key="option">
                              <div>{{option}}</div>
                            </div>
                            <template v-if="!slotProps.value || slotProps.value.length === 0">
                              <div class="p-1">{{ $t("select") }}</div>
                            </template>
                          </template>
                          <template #option="slotProps">
                            <div class="flex align-items-center">
                              <div>{{slotProps.option}}</div>
                            </div>
                          </template>
                        </MultiSelect>
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("referenceLinks") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col-6">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.logo">{{ $t("logo") }}</label>
                        <FileUpload id="createServiceBuffer.logo" mode="basic" name="file" :chooseLabel="$t('selectFile')"
                        @select="fileUploadHelper($event, 'create', 'logo')" accept=".png, .jpg, .jpeg" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.informationUrl">{{ $t("informationURL") }}</label>
                        <InputText id="createServiceBuffer.informationUrl" type="text" v-model="createServiceBuffer.informationUrl" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.privacyUrl">{{ $t("privacyURL") }}</label>
                        <InputText id="createServiceBuffer.privacyUrl" type="text" v-model="createServiceBuffer.privacyUrl" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("contacts") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.contacts.name">{{ $t("name") }}<span style="color: red;"> * </span></label>
                        <InputText id="createServiceBuffer.contacts.name" type="text" :class="createServiceErrors.contacts.name" v-model="createServiceBuffer.contacts[1][0].name" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.contacts.email">{{ $t("email") }}<span style="color: red;"> * </span></label>
                        <InputText id="createServiceBuffer.contacts.email" type="text" :class="createServiceErrors.contacts.email" v-model="createServiceBuffer.contacts[1][0].email" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.contacts.phone">{{ $t("mobile") }}</label>
                        <InputText id="createServiceBuffer.contacts.phone" type="text" v-model="createServiceBuffer.contacts[1][0].phone" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.contacts.department">{{ $t("department") }}</label>
                        <InputText id="createServiceBuffer.contacts.department" type="text" v-model="createServiceBuffer.contacts[1][0].department" />
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("logoutOptions") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.logoutType">{{ $t("logoutType") }}</label>
                        <Dropdown id="createServiceBuffer.logoutType" :options="['NONE','BACK_CHANNEL','FRONT_CHANNEL']" v-model="createServiceBuffer.logoutType" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.logoutUrl">{{ $t("logoutURL") }}</label>
                        <InputText id="createServiceBuffer.logoutUrl" type="text" v-model="createServiceBuffer.logoutUrl" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                  </div>
                  <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="createServiceCheckup()" />
                  <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('createService')" />
                </TabPanel>
                <TabPanel :header="$t('accessSettings')">
                  <h4>{{ $t("groupsAccess") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.accessStrategy.requiredAttributes.ou">{{ $t("groups") }}</label>
                        <MultiSelect id="createServiceBuffer.accessStrategy.requiredAttributes.ou" v-model="createServiceBuffer.accessStrategy.requiredAttributes.ou[1]" :options="groups"
                        :placeholder="$t('select')" :filter="true" :emptyMessage="$t('noGroupsFound')" :emptyFilterMessage="$t('noGroupsFound')" optionLabel="name">
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
                  <h4>{{ $t("usersAccess") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <AppListBox :label="$t('users')" :list="createUsers" :searchParameters="['_id', 'displayName']" :loader="createServiceUsersLoader"
                        secondParameter="displayName" :parentList="true" @toggleRecord="createServiceAllUsersToggle" firstParameter="_id" type="list" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <AppListBox :label="$t('authorizedUsers')" :list="createServiceBuffer.accessStrategy.requiredAttributes.uid[1]" :searchParameters="['_id', 'displayName']" type="authorize"
                        secondParameter="displayName" :parentList="false" @toggleRecord="createServiceAllUsersToggle" firstParameter="_id" :loader="createServiceUsersLoader" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <AppListBox :label="$t('bannedUsers')" :list="createServiceBuffer.accessStrategy.rejectedAttributes.uid[1]" :searchParameters="['_id', 'displayName']" type="ban"
                        secondParameter="displayName" :parentList="false" @toggleRecord="createServiceAllUsersToggle" firstParameter="_id" :loader="createServiceUsersLoader" />
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("dailyBasedAccess") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col-6">
                      <div class="field p-fluid" :dir="$store.state.reverseDirection">
                        <SelectButton v-model="createServiceBuffer.dailyAccessType" :options="[{id: 'URL', name: $t('urlBased')}, {id: 'DAY', name: $t('dayBased')}]" optionLabel="name" />
                      </div>
                    </div>
                  </div>
                  <div v-if="createServiceBuffer.dailyAccessType.id === 'DAY'" class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <AppDayTimeRange scope="create" :checkboxList="createDailyAccessCheckbox" :checkAll="createDailyAccessCheckAll" />
                      </div>
                    </div>
                  </div>
                  <div v-if="createServiceBuffer.dailyAccessType.id === 'URL'" class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.accessStrategy.endpointUrl">{{ $t("url") }}</label>
                        <InputText id="createServiceBuffer.accessStrategy.endpointUrl" type="text" v-model="createServiceBuffer.accessStrategy.endpointUrl" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.accessStrategy.acceptableResponseCodes">{{ $t("acceptableResponseCodes") }}</label>
                        <InputText id="createServiceBuffer.accessStrategy.acceptableResponseCodes" type="text" v-model="createServiceBuffer.accessStrategy.acceptableResponseCodes" placeholder="200,202" />
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("timeBasedAccess") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.startDate">{{ $t("startDate") }}</label>
                        <InputText id="createServiceBuffer.startDate" type="text" class="datePickerFa" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.endDate">{{ $t("endDate") }}</label>
                        <InputText id="createServiceBuffer.endDate" type="text" class="datePickerFa" />
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("attributeBasedAccess") }}</h4>
                  <div v-for="(attribute, index) in createServiceBuffer.attributeList" v-bind:key="index" class="formgrid grid">
                    <div class="field col-2">
                      <div class="field p-fluid">
                        <label v-if="index === 0">{{ $t("attributeName") }}</label>
                        <InputText v-model="attribute.name" type="text" placeholder="name" />
                      </div>
                    </div>
                    <div class="field col-8">
                      <div class="field p-fluid">
                        <label v-if="index === 0">{{ $t("attributeValue") }}</label>
                        <InputText v-model="attribute.value" type="text" placeholder="value1,value2,value3" />
                      </div>
                    </div>
                    <div class="field col-2">
                      <div class="field p-fluid">
                        <Button v-if="index !== 0" @click="attributeHelper('create', 'remove', index)" icon="bx bx-x bx-sm" v-tooltip.top="$t('delete')" class="p-button-rounded p-button-danger p-button-outlined p-button-sm" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col-2">
                      <div class="field p-fluid">
                        <Button :label="$t('add')" icon="bx bx-plus bx-sm" class="p-button-success" @click="attributeHelper('create', 'add')" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col-6">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.accessStrategy.unauthorizedRedirectUrl">{{ $t("unauthorizedRedirectUrl") }}</label>
                        <InputText id="createServiceBuffer.accessStrategy.unauthorizedRedirectUrl" type="text" v-model="createServiceBuffer.accessStrategy.unauthorizedRedirectUrl" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                  </div>
                  <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="createServiceCheckup()" />
                  <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('createService')" />
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
                        <Dropdown id="createServiceBuffer.multifactorPolicy.failureMode" :options="['NONE', 'CLOSED', 'OPEN', 'PHANTOM']"
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
                        <InputText id="createServiceBuffer.extraInfo.notificationApiURL" type="text" v-model="createServiceBuffer.extraInfo.notificationApiURL" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
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
            <BlockUI :blocked="editServiceLoader">
              <TabView v-model:activeIndex="tabActiveIndexEdit">
                <TabPanel :header="$t('basicSettings')">
                  <h4>{{ $t("serviceInformation") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col-6">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.id">{{ $t("id") }}</label>
                        <InputText id="editServiceBuffer.id" type="text" v-model="editServiceBuffer.id" :disabled="true" />
                      </div>
                    </div>
                    <div class="field col-3">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.accessStrategy.enabled" class="flex">{{ $t("enableService") }}</label>
                        <InputSwitch id="editServiceBuffer.accessStrategy.enabled" v-model="editServiceBuffer.accessStrategy.enabled" />
                      </div>
                    </div>
                    <div class="field col-3">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.accessStrategy.ssoEnabled" class="flex">{{ $t("allowSSO") }}</label>
                        <InputSwitch id="editServiceBuffer.accessStrategy.ssoEnabled" v-model="editServiceBuffer.accessStrategy.ssoEnabled" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.name">{{ $t("serviceName") }}<span style="color: red;"> * </span></label>
                        <InputText id="editServiceBuffer.name" type="text" :class="editServiceErrors.name" v-model="editServiceBuffer.name" @keypress="englishInputFilter($event)" @paste="englishInputFilter($event)" />
                        <small>{{ $t("inputEnglishFilterText") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.description">{{ $t("serviceFarsiName") }}<span style="color: red;"> * </span></label>
                        <InputText id="editServiceBuffer.description" type="text" :class="editServiceErrors.description" v-model="editServiceBuffer.description" @keypress="persianInputFilter($event)" @paste="persianInputFilter($event)" />
                        <small>{{ $t("inputPersianFilterText") }}</small>
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.serviceId">{{ $t("serviceID") }}<span style="color: red;"> * </span></label>
                        <InputText id="editServiceBuffer.serviceId" type="text" :class="editServiceErrors.serviceId" v-model="editServiceBuffer.serviceId" dir="ltr" />
                        <small>{{ $t("serviceURLText1") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.extraInfo.url">{{ $t("serviceURL") }}<span style="color: red;"> * </span></label>
                        <InputText id="editServiceBuffer.extraInfo.url" type="text" :class="editServiceErrors.extraInfo.url" v-model="editServiceBuffer.extraInfo.url" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                  </div>
                  <div v-if="editServiceBuffer.serviceType === 'SAML'" class="formgrid grid">
                    <div class="field col-2">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.metadataLocation" class="flex">Metadata Location</label>
                        <div class="field-radiobutton">
                          <RadioButton id="metadataLocationAddress" name="metadataLocation" value="address" v-model="editServiceBuffer.metadataLocationOption" />
                          <label for="metadataLocationAddress" class="mx-2">{{ $t("url") }}</label>
                        </div>
                        <div class="field-radiobutton">
                          <RadioButton id="metadataLocationFile" name="metadataLocation" value="file" v-model="editServiceBuffer.metadataLocationOption" />
                          <label for="metadataLocationFile" class="mx-2">{{ $t("file") }}</label>
                        </div>
                      </div>
                    </div>
                    <div class="field col-6">
                      <div v-if="editServiceBuffer.metadataLocationOption === 'address'" class="field p-fluid">
                        <InputText id="editServiceBuffer.metadataLocation" type="text" :class="editServiceErrors.metadataLocation" v-model="editServiceBuffer.metadataLocation" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                      <div v-else-if="editServiceBuffer.metadataLocationOption === 'file'" class="field p-fluid">
                        <FileUpload id="editServiceBuffer.metadataLocation" mode="basic" name="file" :chooseLabel="$t('selectFile')"
                        @select="fileUploadHelper($event, 'edit', 'metadata')" :class="editServiceErrors.metadataLocation" accept=".xml" />
                      </div>
                    </div>
                  </div>
                  <div v-if="editServiceBuffer.serviceType === 'Oauth2'" class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.clientId">Client Id<span style="color: red;"> * </span></label>
                        <InputText id="editServiceBuffer.clientId" type="text" :class="editServiceErrors.clientId" v-model="editServiceBuffer.clientId" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.clientSecret">Client Secret<span style="color: red;"> * </span></label>
                        <div class="p-inputgroup">
                          <InputText id="editServiceBuffer.clientSecret" v-model="editServiceBuffer.clientSecret" :class="editServiceErrors.clientSecret" />
                          <Button @click="generateSecret('edit')">{{ $t("generateSecret") }}</Button>
                        </div>
                      </div>
                    </div>
                  </div>
                  <div v-if="editServiceBuffer.serviceType === 'Oauth2'" class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.supportedGrantTypes">Supported Grant Types<span style="color: red;"> * </span></label>
                        <MultiSelect id="editServiceBuffer.supportedGrantTypes" v-model="editServiceBuffer.supportedGrantTypes[1]" :class="editServiceErrors.supportedGrantTypes"
                        :options="['none', 'authorization_code', 'password', 'client_credentials', 'refresh_token', 'uma_ticket']" :placeholder="$t('select')">
                          <template #value="slotProps">
                            <div class="inline-flex align-items-center py-1 px-2 bg-primary text-primary border-round mr-2" v-for="option of slotProps.value" :key="option">
                              <div>{{option}}</div>
                            </div>
                            <template v-if="!slotProps.value || slotProps.value.length === 0">
                              <div class="p-1">{{ $t("select") }}</div>
                            </template>
                          </template>
                          <template #option="slotProps">
                            <div class="flex align-items-center">
                              <div>{{slotProps.option}}</div>
                            </div>
                          </template>
                        </MultiSelect>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.supportedResponseTypes">Supported Response Types<span style="color: red;"> * </span></label>
                        <MultiSelect id="editServiceBuffer.supportedResponseTypes" v-model="editServiceBuffer.supportedResponseTypes[1]" :class="editServiceErrors.supportedResponseTypes"
                        :options="['none', 'code', 'token', 'device_code', 'idtoken_token', 'id_token']" :placeholder="$t('select')">
                          <template #value="slotProps">
                            <div class="inline-flex align-items-center py-1 px-2 bg-primary text-primary border-round mr-2" v-for="option of slotProps.value" :key="option">
                              <div>{{option}}</div>
                            </div>
                            <template v-if="!slotProps.value || slotProps.value.length === 0">
                              <div class="p-1">{{ $t("select") }}</div>
                            </template>
                          </template>
                          <template #option="slotProps">
                            <div class="flex align-items-center">
                              <div>{{slotProps.option}}</div>
                            </div>
                          </template>
                        </MultiSelect>
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("referenceLinks") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.logoAddress">{{ $t("logo") }}</label>
                        <InputText id="editServiceBuffer.logoAddress" type="text" v-model="editServiceBuffer.logo" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.logo">&nbsp;</label>
                        <FileUpload id="editServiceBuffer.logo" mode="basic" name="file" :chooseLabel="$t('selectFile')"
                        @select="fileUploadHelper($event, 'edit', 'logo')" accept=".png, .jpg, .jpeg" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.informationUrl">{{ $t("informationURL") }}</label>
                        <InputText id="editServiceBuffer.informationUrl" type="text" v-model="editServiceBuffer.informationUrl" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.privacyUrl">{{ $t("privacyURL") }}</label>
                        <InputText id="editServiceBuffer.privacyUrl" type="text" v-model="editServiceBuffer.privacyUrl" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("contacts") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.contacts.name">{{ $t("name") }}<span style="color: red;"> * </span></label>
                        <InputText id="editServiceBuffer.contacts.name" type="text" :class="editServiceErrors.contacts.name" v-model="editServiceBuffer.contacts[1][0].name" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.contacts.email">{{ $t("email") }}<span style="color: red;"> * </span></label>
                        <InputText id="editServiceBuffer.contacts.email" type="text" :class="editServiceErrors.contacts.email" v-model="editServiceBuffer.contacts[1][0].email" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.contacts.phone">{{ $t("mobile") }}</label>
                        <InputText id="editServiceBuffer.contacts.phone" type="text" v-model="editServiceBuffer.contacts[1][0].phone" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.contacts.department">{{ $t("department") }}</label>
                        <InputText id="editServiceBuffer.contacts.department" type="text" v-model="editServiceBuffer.contacts[1][0].department" />
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("logoutOptions") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.logoutType">{{ $t("logoutType") }}</label>
                        <Dropdown id="editServiceBuffer.logoutType" :options="['NONE','BACK_CHANNEL','FRONT_CHANNEL']" v-model="editServiceBuffer.logoutType" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.logoutUrl">{{ $t("logoutURL") }}</label>
                        <InputText id="editServiceBuffer.logoutUrl" type="text" v-model="editServiceBuffer.logoutUrl" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                  </div>
                  <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="editServiceCheckup()" />
                  <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('editService')" />
                </TabPanel>
                <TabPanel :header="$t('accessSettings')">
                  <h4>{{ $t("groupsAccess") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.accessStrategy.requiredAttributes.ou">{{ $t("groups") }}</label>
                        <MultiSelect id="editServiceBuffer.accessStrategy.requiredAttributes.ou" v-model="editServiceBuffer.accessStrategy.requiredAttributes.ou[1]" :options="groups"
                        :placeholder="$t('select')" :filter="true" :emptyMessage="$t('noGroupsFound')" :emptyFilterMessage="$t('noGroupsFound')" optionLabel="name">
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
                  <h4>{{ $t("usersAccess") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <AppListBox :label="$t('users')" :list="editUsers" :searchParameters="['_id', 'displayName']" :loader="editServiceUsersLoader"
                        secondParameter="displayName" :parentList="true" @toggleRecord="editServiceAllUsersToggle" firstParameter="_id" type="list" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <AppListBox :label="$t('authorizedUsers')" :list="editServiceBuffer.accessStrategy.requiredAttributes.uid[1]" :searchParameters="['_id', 'displayName']" type="authorize"
                        secondParameter="displayName" :parentList="false" @toggleRecord="editServiceAllUsersToggle" firstParameter="_id" :loader="editServiceUsersLoader" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <AppListBox :label="$t('bannedUsers')" :list="editServiceBuffer.accessStrategy.rejectedAttributes.uid[1]" :searchParameters="['_id', 'displayName']" type="ban"
                        secondParameter="displayName" :parentList="false" @toggleRecord="editServiceAllUsersToggle" firstParameter="_id" :loader="editServiceUsersLoader" />
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("dailyBasedAccess") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col-6">
                      <div class="field p-fluid" :dir="$store.state.reverseDirection">
                        <SelectButton v-model="editServiceBuffer.dailyAccessType" :options="[{id: 'URL', name: $t('urlBased')}, {id: 'DAY', name: $t('dayBased')}]" optionLabel="name" />
                      </div>
                    </div>
                  </div>
                  <div v-if="editServiceBuffer.dailyAccessType.id === 'DAY'" class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <AppDayTimeRange scope="edit" :checkboxList="editDailyAccessCheckbox" :checkAll="editDailyAccessCheckAll" />
                      </div>
                    </div>
                  </div>
                  <div v-if="editServiceBuffer.dailyAccessType.id === 'URL'" class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.accessStrategy.endpointUrl">{{ $t("url") }}</label>
                        <InputText id="editServiceBuffer.accessStrategy.endpointUrl" type="text" v-model="editServiceBuffer.accessStrategy.endpointUrl" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.accessStrategy.acceptableResponseCodes">{{ $t("acceptableResponseCodes") }}</label>
                        <InputText id="editServiceBuffer.accessStrategy.acceptableResponseCodes" type="text" v-model="editServiceBuffer.accessStrategy.acceptableResponseCodes" placeholder="200,202" />
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("timeBasedAccess") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.startDate">{{ $t("startDate") }}</label>
                        <InputText id="editServiceBuffer.startDate" type="text" class="datePickerFa" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.endDate">{{ $t("endDate") }}</label>
                        <InputText id="editServiceBuffer.endDate" type="text" class="datePickerFa" />
                      </div>
                    </div>
                  </div>
                  <h4>{{ $t("attributeBasedAccess") }}</h4>
                  <div v-for="(attribute, index) in editServiceBuffer.attributeList" v-bind:key="index" class="formgrid grid">
                    <div class="field col-2">
                      <div class="field p-fluid">
                        <label v-if="index === 0">{{ $t("attributeName") }}</label>
                        <InputText v-model="attribute.name" type="text" placeholder="name" />
                      </div>
                    </div>
                    <div class="field col-8">
                      <div class="field p-fluid">
                        <label v-if="index === 0">{{ $t("attributeValue") }}</label>
                        <InputText v-model="attribute.value" type="text" placeholder="value1,value2,value3" />
                      </div>
                    </div>
                    <div class="field col-2">
                      <div class="field p-fluid">
                        <Button v-if="index !== 0" @click="attributeHelper('edit', 'remove', index)" icon="bx bx-x bx-sm" v-tooltip.top="$t('delete')" class="p-button-rounded p-button-danger p-button-outlined p-button-sm" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col-2">
                      <div class="field p-fluid">
                        <Button :label="$t('add')" icon="bx bx-plus bx-sm" class="p-button-success" @click="attributeHelper('edit', 'add')" />
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col-6">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.accessStrategy.unauthorizedRedirectUrl">{{ $t("unauthorizedRedirectUrl") }}</label>
                        <InputText id="editServiceBuffer.accessStrategy.unauthorizedRedirectUrl" type="text" v-model="editServiceBuffer.accessStrategy.unauthorizedRedirectUrl" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                  </div>
                  <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="editServiceCheckup()" />
                  <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('editService')" />
                </TabPanel>
                <TabPanel :header="$t('authenticationSettings')">
                  <h4>{{ $t("multi-factorAuthentication") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders">{{ $t("activation") }}</label>
                        <Dropdown id="editServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders"
                        v-model="editServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders"
                        :options="multifactorAuthenticationProvidersOptions" optionLabel="name" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.multifactorPolicy.failureMode">Failure Mode</label>
                        <Dropdown id="editServiceBuffer.multifactorPolicy.failureMode" :options="['NONE', 'CLOSED', 'OPEN', 'PHANTOM']"
                        v-model="editServiceBuffer.multifactorPolicy.failureMode" />
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid" style="display: grid;">
                        <label for="editServiceBuffer.multifactorPolicy.bypassEnabled">Bypass Enabled</label>
                        <ToggleButton id="editServiceBuffer.multifactorPolicy.bypassEnabled" onIcon="pi pi-check"
                        v-model="editServiceBuffer.multifactorPolicy.bypassEnabled" offIcon="pi pi-times" />
                      </div>
                    </div>
                  </div>
                  <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="editServiceCheckup()" />
                  <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('editService')" />
                </TabPanel>
                <TabPanel :header="$t('notificationSettings')">
                  <h4>{{ $t("notifications") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.extraInfo.notificationApiURL">{{ $t("apiAddress") }}</label>
                        <InputText id="editServiceBuffer.extraInfo.notificationApiURL" type="text" v-model="editServiceBuffer.extraInfo.notificationApiURL" dir="ltr" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="editServiceBuffer.extraInfo.notificationApiKey">{{ $t("apiKey") }}</label>
                        <InputText id="editServiceBuffer.extraInfo.notificationApiKey" type="text" v-model="editServiceBuffer.extraInfo.notificationApiKey" />
                      </div>
                    </div>
                  </div>
                  <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="editServiceCheckup()" />
                  <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('editService')" />
                </TabPanel>
              </TabView>
            </BlockUI>
          </TabPanel>
        </TabView>
      </div>
    </div>
  </div>
</template>

<script>
import "persian-datepicker/dist/js/persian-datepicker"
import AppListBox from "@/components/AppListBox.vue"
import AppDayTimeRange from "@/components/AppDayTimeRange.vue"
import iziToast from "@/assets/scripts/iziToast.min.js"
import JSPDF from "jspdf/dist/jspdf.umd.min.js"
import autoTable from "jspdf-autotable/dist/jspdf.plugin.autotable.min.js"

export default {
  name: "Services",
  components: {
    AppListBox,
    AppDayTimeRange
  },
  data () {
    return {
      services: [],
      groups: [],
      users: [],
      createUsers: [],
      editUsers: [],
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
      createServiceErrors: {
        name: "",
        serviceId: "",
        description: "",
        metadataLocation: "",
        clientId: "",
        clientSecret: "",
        supportedGrantTypes: "",
        supportedResponseTypes: "",
        contacts: {
          name: "",
          email: ""
        },
        extraInfo: {
          url: ""
        }
      },
      editServiceErrors: {
        name: "",
        serviceId: "",
        description: "",
        metadataLocation: "",
        clientId: "",
        clientSecret: "",
        supportedGrantTypes: "",
        supportedResponseTypes: "",
        contacts: {
          name: "",
          email: ""
        },
        extraInfo: {
          url: ""
        }
      },
      createServiceBuffer: {
        serviceType: "CAS",
        logoFile: null,
        metadataFile: null,
        dailyAccessType: {
          id: "DAY",
          name: this.$t("dayBased")
        },
        attributeList: [
          {
            name: "",
            value: ""
          }
        ],
        name: "",
        serviceId: "",
        description: "",
        metadataLocation: "",
        metadataLocationOption: "address",
        clientId: "",
        clientSecret: "",
        supportedGrantTypes: [
          "java.util.HashSet",
          ["authorization_code"]
        ],
        supportedResponseTypes: [
          "java.util.HashSet",
          ["code"]
        ],
        logo: null,
        informationUrl: null,
        privacyUrl: null,
        logoutType: "BACK_CHANNEL",
        logoutUrl: null,
        contacts: [
          "java.util.ArrayList",
          [
            {
              name: "",
              email: "",
              phone: null,
              department: null
            }
          ]
        ],
        accessStrategy: {
          enabled: true,
          ssoEnabled: true,
          unauthorizedRedirectUrl: null,
          endpointUrl: null,
          acceptableResponseCodes: null,
          requiredAttributes: {
            uid: [
              "java.util.HashSet",
              []
            ],
            ou: [
              "java.util.HashSet",
              []
            ]
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
          url: "",
          dailyAccess: null,
          notificationApiURL: null,
          notificationApiKey: null
        }
      },
      editServiceBuffer: {
        serviceType: "CAS",
        logoFile: null,
        metadataFile: null,
        dailyAccessType: {
          id: "DAY",
          name: this.$t("dayBased")
        },
        attributeList: [
          {
            name: "",
            value: ""
          }
        ],
        id: "",
        name: "",
        serviceId: "",
        description: "",
        metadataLocation: "",
        metadataLocationOption: "address",
        clientId: "",
        clientSecret: "",
        supportedGrantTypes: [
          "java.util.HashSet",
          ["authorization_code"]
        ],
        supportedResponseTypes: [
          "java.util.HashSet",
          ["code"]
        ],
        logo: null,
        informationUrl: null,
        privacyUrl: null,
        logoutType: "BACK_CHANNEL",
        logoutUrl: null,
        contacts: [
          "java.util.ArrayList",
          [
            {
              name: "",
              email: "",
              phone: null,
              department: null
            }
          ]
        ],
        accessStrategy: {
          enabled: true,
          ssoEnabled: true,
          unauthorizedRedirectUrl: null,
          endpointUrl: null,
          acceptableResponseCodes: null,
          requiredAttributes: {
            uid: [
              "java.util.HashSet",
              []
            ],
            ou: [
              "java.util.HashSet",
              []
            ]
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
          url: "",
          dailyAccess: null,
          notificationApiURL: null,
          notificationApiKey: null
        }
      },
      reportedService: {},
      allowedUsersReportList: [],
      bannedUsersReportList: [],
      allowedGroupsReportList: [],
      bannedGroupsReportList: [],
      usersReportList: [],
      groupsReportList: [],
      createDailyAccessCheckbox: [false, false, false, false, false, false, false],
      editDailyAccessCheckbox: [false, false, false, false, false, false, false],
      createDailyAccessCheckAll: false,
      editDailyAccessCheckAll: false,
      tabActiveIndexList: 0,
      tabActiveIndexCreate: 0,
      tabActiveIndexEdit: 0,
      serviceToolbarBuffer: "",
      loading: false,
      createServiceFlag: false,
      editServiceFlag: false,
      createServiceLoader: false,
      createServiceUsersLoader: false,
      editServiceLoader: false,
      editServiceUsersLoader: false,
      persianDate: null,
      persianRex: null,
      XLSX: null
    }
  },
  mounted () {
    this.persianDate = require("persian-date/dist/persian-date")
    this.persianRex = require("persian-rex/dist/persian-rex")
    this.XLSX = require("xlsx/dist/xlsx.full.min.js")
    this.servicesRequestMaster("getServices")
  },
  methods: {
    toggleExportAccessReports (event, id) {
      this.$refs["exportAccessReports" + id].toggle(event)
    },
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
          url: "/api/services/" + vm.editServiceBuffer.id,
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.editServiceBuffer.accessStrategy.enabled = res.data.data.accessStrategy.enabled
            vm.editServiceBuffer.accessStrategy.ssoEnabled = res.data.data.accessStrategy.ssoEnabled
            vm.editServiceBuffer.name = res.data.data.name
            res.data.data.serviceId = res.data.data.serviceId.replace(/\\/g, "\\\\")
            vm.editServiceBuffer.serviceId = res.data.data.serviceId
            vm.editServiceBuffer.description = res.data.data.description
            if (typeof res.data.data.extraInfo !== "undefined") {
              vm.editServiceBuffer.extraInfo.url = res.data.data.extraInfo.url
            }
            if (typeof res.data.data.metadataLocation !== "undefined") {
              vm.editServiceBuffer.serviceType = "SAML"
              vm.editServiceBuffer.metadataLocation = res.data.data.metadataLocation
            }
            if (typeof res.data.data.clientId !== "undefined" && typeof res.data.data.clientSecret !== "undefined") {
              vm.editServiceBuffer.serviceType = "Oauth2"
              vm.editServiceBuffer.clientId = res.data.data.clientId
              vm.editServiceBuffer.clientSecret = res.data.data.clientSecret
              vm.editServiceBuffer.supportedGrantTypes[1] = res.data.data.supportedGrantTypes[1]
              vm.editServiceBuffer.supportedResponseTypes[1] = res.data.data.supportedResponseTypes[1]
            }

            if (typeof res.data.data.logo !== "undefined") {
              vm.editServiceBuffer.logo = res.data.data.logo
            }
            if (typeof res.data.data.informationUrl !== "undefined") {
              vm.editServiceBuffer.informationUrl = res.data.data.informationUrl
            }
            if (typeof res.data.data.privacyUrl !== "undefined") {
              vm.editServiceBuffer.privacyUrl = res.data.data.privacyUrl
            }

            vm.editServiceBuffer.contacts[1][0].name = res.data.data.contacts[1][0].name
            vm.editServiceBuffer.contacts[1][0].email = res.data.data.contacts[1][0].email
            if (typeof res.data.data.contacts[1][0].phone !== "undefined") {
              vm.editServiceBuffer.contacts[1][0].phone = res.data.data.contacts[1][0].phone
            }
            if (typeof res.data.data.contacts[1][0].department !== "undefined") {
              vm.editServiceBuffer.contacts[1][0].department = res.data.data.contacts[1][0].department
            }

            if (typeof res.data.data.logoutUrl !== "undefined") {
              vm.editServiceBuffer.logoutUrl = res.data.data.logoutUrl
            }
            vm.editServiceBuffer.logoutType = res.data.data.logoutType

            if (typeof res.data.data.accessStrategy.unauthorizedRedirectUrl !== "undefined") {
              vm.editServiceBuffer.accessStrategy.unauthorizedRedirectUrl = res.data.data.accessStrategy.unauthorizedRedirectUrl
            }

            if (typeof res.data.data.accessStrategy.requiredAttributes !== "undefined") {
              if (typeof res.data.data.accessStrategy.requiredAttributes.ou !== "undefined") {
                for (const i in res.data.data.accessStrategy.requiredAttributes.ou[1]) {
                  for (const j in vm.groups) {
                    if (res.data.data.accessStrategy.requiredAttributes.ou[1][i] === vm.groups[j].id) {
                      vm.editServiceBuffer.accessStrategy.requiredAttributes.ou[1].push(vm.groups[j])
                      break
                    }
                  }
                }
              }
              if (typeof res.data.data.accessStrategy.requiredAttributes.uid !== "undefined") {
                for (const i in res.data.data.accessStrategy.requiredAttributes.uid[1]) {
                  for (const j in vm.users) {
                    if (res.data.data.accessStrategy.requiredAttributes.uid[1][i] === vm.users[j]._id) {
                      vm.editServiceBuffer.accessStrategy.requiredAttributes.uid[1].push(vm.users[j])
                      break
                    }
                  }
                }
              }
              const tempAattributeList = []
              const tempRequiredAttribute = Object.entries(res.data.data.accessStrategy.requiredAttributes)
              for (const i in tempRequiredAttribute) {
                if (tempRequiredAttribute[i][0] !== "@class" && tempRequiredAttribute[i][0] !== "ou" && tempRequiredAttribute[i][0] !== "uid") {
                  tempAattributeList.push({ name: tempRequiredAttribute[i][0], value: tempRequiredAttribute[i][1][1].join(",") })
                }
              }
              if (tempAattributeList.length > 0) {
                vm.editServiceBuffer.attributeList = tempAattributeList
              }
            }

            if (typeof res.data.data.accessStrategy.rejectedAttributes !== "undefined") {
              if (typeof res.data.data.accessStrategy.rejectedAttributes.uid !== "undefined") {
                for (const i in res.data.data.accessStrategy.rejectedAttributes.uid[1]) {
                  for (const j in vm.users) {
                    if (res.data.data.accessStrategy.rejectedAttributes.uid[1][i] === vm.users[j]._id) {
                      vm.editServiceBuffer.accessStrategy.rejectedAttributes.uid[1].push(vm.users[j])
                      break
                    }
                  }
                }
              }
            }

            if (typeof res.data.data.extraInfo !== "undefined") {
              if (typeof res.data.data.extraInfo.dailyAccess !== "undefined") {
                vm.editServiceBuffer.dailyAccessType = { id: "DAY", name: vm.$t("dayBased") }
                if (res.data.data.extraInfo.dailyAccess.length > 0) {
                  for (const i in res.data.data.extraInfo.dailyAccess) {
                    vm.editDailyAccessCheckbox[res.data.data.extraInfo.dailyAccess[i].weekDay] = true
                    document.getElementById("editServiceBuffer.dailyAccess.start" + String(res.data.data.extraInfo.dailyAccess[i].weekDay)).value =
                      vm.enNumToFaNum(String(res.data.data.extraInfo.dailyAccess[i].period.from.hour)) + ":" + vm.enNumToFaNum(String(res.data.data.extraInfo.dailyAccess[i].period.from.minute))
                    document.getElementById("editServiceBuffer.dailyAccess.end" + String(res.data.data.extraInfo.dailyAccess[i].weekDay)).value =
                      vm.enNumToFaNum(String(res.data.data.extraInfo.dailyAccess[i].period.to.hour)) + ":" + vm.enNumToFaNum(String(res.data.data.extraInfo.dailyAccess[i].period.to.minute))
                  }
                  if (res.data.data.extraInfo.dailyAccess.length === 7) {
                    vm.editDailyAccessCheckAll = true
                  }
                }
              } else if (typeof res.data.data.accessStrategy.endpointUrl !== "undefined") {
                vm.editServiceBuffer.dailyAccessType = { id: "URL", name: vm.$t("urlBased") }
                vm.editServiceBuffer.accessStrategy.endpointUrl = res.data.data.accessStrategy.endpointUrl
                if (typeof res.data.data.accessStrategy.acceptableResponseCodes !== "undefined") {
                  vm.editServiceBuffer.accessStrategy.acceptableResponseCodes = res.data.data.accessStrategy.acceptableResponseCodes
                }
              }
            }

            if (typeof res.data.data.accessStrategy.startingDateTime !== "undefined") {
              const seTime = res.data.data.accessStrategy.startingDateTime
              vm.persianDate.toCalendar("gregorian")
              // eslint-disable-next-line new-cap
              const dayWrapper = new vm.persianDate([seTime.substring(0, 4), seTime.substring(5, 7), seTime.substring(8, 10),
                seTime.substring(11, 13), seTime.substring(14, 16), seTime.substring(17, 19), seTime.substring(20, 23)])
              document.getElementById("editServiceBuffer.startDate").value = dayWrapper.toCalendar("persian").format("dddd DD MMMM YYYY  HH:mm  a")
            }
            if (typeof res.data.data.accessStrategy.endingDateTime !== "undefined") {
              const seTime = res.data.data.accessStrategy.endingDateTime
              vm.persianDate.toCalendar("gregorian")
              // eslint-disable-next-line new-cap
              const dayWrapper = new vm.persianDate([seTime.substring(0, 4), seTime.substring(5, 7), seTime.substring(8, 10),
                seTime.substring(11, 13), seTime.substring(14, 16), seTime.substring(17, 19), seTime.substring(20, 23)])
              document.getElementById("editServiceBuffer.endDate").value = dayWrapper.toCalendar("persian").format("dddd DD MMMM YYYY  HH:mm  a")
            }

            if (typeof res.data.data.multifactorPolicy !== "undefined") {
              if (typeof res.data.data.multifactorPolicy.multifactorAuthenticationProviders !== "undefined") {
                if (res.data.data.multifactorPolicy.multifactorAuthenticationProviders[0] === "java.util.LinkedHashSet" &&
                res.data.data.multifactorPolicy.multifactorAuthenticationProviders[1][0] === "[\"java.util.LinkedHashSet\",[\"mfa-simple\"]]") {
                  vm.editServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders = { value: "mfa-simple", name: vm.$t("smsCode") }
                } else if (res.data.data.multifactorPolicy.multifactorAuthenticationProviders[0] === "java.util.LinkedHashSet" &&
                res.data.data.multifactorPolicy.multifactorAuthenticationProviders[1][0] === "[\"java.util.LinkedHashSet\",[\"mfa-gauth\"]]") {
                  vm.editServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders = { value: "mfa-gauth", name: this.$t("oneTimePassword") }
                } else if (res.data.data.multifactorPolicy.multifactorAuthenticationProviders[0] === "java.util.LinkedHashSet" &&
                res.data.data.multifactorPolicy.multifactorAuthenticationProviders[1][0] === "[\"java.util.LinkedHashSet\",[\"mfa-u2f\"]]") {
                  vm.editServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders = { value: "mfa-u2f", name: this.$t("hardwareToken") }
                }
              }
              if (typeof res.data.data.multifactorPolicy.bypassEnabled !== "undefined") {
                vm.editServiceBuffer.multifactorPolicy.bypassEnabled = res.data.data.multifactorPolicy.bypassEnabled
              }
              if (typeof res.data.data.multifactorPolicy.failureMode !== "undefined") {
                vm.editServiceBuffer.multifactorPolicy.failureMode = res.data.data.multifactorPolicy.failureMode
              }
            }

            if (typeof res.data.data.extraInfo !== "undefined") {
              if (typeof res.data.data.extraInfo.notificationApiURL !== "undefined") {
                vm.editServiceBuffer.extraInfo.notificationApiURL = res.data.data.extraInfo.notificationApiURL
              }
              if (typeof res.data.data.extraInfo.notificationApiKey !== "undefined") {
                vm.editServiceBuffer.extraInfo.notificationApiKey = res.data.data.extraInfo.notificationApiKey
              }
            }
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
        let serviceData = {}
        this.createServiceLoader = true
        const ouTemp = []
        if (typeof this.createServiceBuffer.accessStrategy.requiredAttributes.ou[1] !== "undefined") {
          for (const i in this.createServiceBuffer.accessStrategy.requiredAttributes.ou[1]) {
            ouTemp.push(this.createServiceBuffer.accessStrategy.requiredAttributes.ou[1][i].id)
          }
          this.createServiceBuffer.accessStrategy.requiredAttributes.ou[1] = ouTemp
        }
        const userAddTemp = []
        if (typeof this.createServiceBuffer.accessStrategy.requiredAttributes.uid[1] !== "undefined") {
          for (const i in this.createServiceBuffer.accessStrategy.requiredAttributes.uid[1]) {
            userAddTemp.push(this.createServiceBuffer.accessStrategy.requiredAttributes.uid[1][i]._id)
          }
          this.createServiceBuffer.accessStrategy.requiredAttributes.uid[1] = userAddTemp
        }
        const userBanTemp = []
        if (typeof this.createServiceBuffer.accessStrategy.rejectedAttributes.uid[1] !== "undefined") {
          for (const i in this.createServiceBuffer.accessStrategy.rejectedAttributes.uid[1]) {
            userBanTemp.push(this.createServiceBuffer.accessStrategy.rejectedAttributes.uid[1][i]._id)
          }
          this.createServiceBuffer.accessStrategy.rejectedAttributes.uid[1] = userBanTemp
        }
        const dailyAccessTemp = []
        for (let i = 0; i < 7; ++i) {
          if (document.getElementById("createSelect" + i).checked) {
            const tempDailyAccessStart = document.getElementById("createServiceBuffer.dailyAccess.start" + i).value.split(":")
            const tempDailyAccessEnd = document.getElementById("createServiceBuffer.dailyAccess.end" + i).value.split(":")
            if (tempDailyAccessStart[0] === "") {
              tempDailyAccessStart[0] = "0"
              tempDailyAccessStart.push("00")
            } else if (typeof tempDailyAccessStart[1] === "undefined") {
              tempDailyAccessStart.push("00")
            }
            if (tempDailyAccessEnd[0] === "") {
              tempDailyAccessEnd[0] = "23"
              tempDailyAccessEnd.push("59")
            } else if (typeof tempDailyAccessEnd[1] === "undefined") {
              tempDailyAccessEnd.push("00")
            }
            tempDailyAccessStart[0] = this.faNumToEnNum(tempDailyAccessStart[0])
            tempDailyAccessStart[1] = this.faNumToEnNum(tempDailyAccessStart[1])
            tempDailyAccessEnd[0] = this.faNumToEnNum(tempDailyAccessEnd[0])
            tempDailyAccessEnd[1] = this.faNumToEnNum(tempDailyAccessEnd[1])
            const tempDailyAccessDay = {
              weekDay: i,
              period: {
                from: {
                  hour: tempDailyAccessStart[0],
                  minute: tempDailyAccessStart[1]
                },
                to: {
                  hour: tempDailyAccessEnd[0],
                  minute: tempDailyAccessEnd[1]
                }
              }
            }
            dailyAccessTemp.push(tempDailyAccessDay)
          }
        }
        if (dailyAccessTemp.length > 0) {
          this.createServiceBuffer.extraInfo.dailyAccess = dailyAccessTemp
        }
        if (document.getElementById("createServiceBuffer.startDate").value !== "" && document.getElementById("createServiceBuffer.endDate").value !== "") {
          const dateStartTemp = document.getElementById("createServiceBuffer.startDate").value.split("  ")
          const dateStart = dateStartTemp[0].split(" ")
          let dateStartFinal
          dateStart[dateStart.length - 1] = this.faNumToEnNum(dateStart[dateStart.length - 1])
          dateStart[dateStart.length - 3] = this.faNumToEnNum(dateStart[dateStart.length - 3])

          switch (dateStart[dateStart.length - 2]) {
            case "فروردین":
              dateStartFinal = dateStart[dateStart.length - 1] + "-01-" + dateStart[dateStart.length - 3]
              break
            case "اردیبهشت":
              dateStartFinal = dateStart[dateStart.length - 1] + "-02-" + dateStart[dateStart.length - 3]
              break
            case "خرداد":
              dateStartFinal = dateStart[dateStart.length - 1] + "-03-" + dateStart[dateStart.length - 3]
              break
            case "تیر":
              dateStartFinal = dateStart[dateStart.length - 1] + "-04-" + dateStart[dateStart.length - 3]
              break
            case "مرداد":
              dateStartFinal = dateStart[dateStart.length - 1] + "-05-" + dateStart[dateStart.length - 3]
              break
            case "شهریور":
              dateStartFinal = dateStart[dateStart.length - 1] + "-06-" + dateStart[dateStart.length - 3]
              break
            case "مهر":
              dateStartFinal = dateStart[dateStart.length - 1] + "-07-" + dateStart[dateStart.length - 3]
              break
            case "آبان":
              dateStartFinal = dateStart[dateStart.length - 1] + "-08-" + dateStart[dateStart.length - 3]
              break
            case "آذر":
              dateStartFinal = dateStart[dateStart.length - 1] + "-09-" + dateStart[dateStart.length - 3]
              break
            case "دی":
              dateStartFinal = dateStart[dateStart.length - 1] + "-10-" + dateStart[dateStart.length - 3]
              break
            case "بهمن":
              dateStartFinal = dateStart[dateStart.length - 1] + "-11-" + dateStart[dateStart.length - 3]
              break
            case "اسفند":
              dateStartFinal = dateStart[dateStart.length - 1] + "-12-" + dateStart[dateStart.length - 3]
              break
            default:
              console.log("Wrong Input for Month")
          }

          const dateEndTemp = document.getElementById("createServiceBuffer.endDate").value.split("  ")
          const dateEnd = dateEndTemp[0].split(" ")
          let dateEndFinal
          dateEnd[dateEnd.length - 1] = this.faNumToEnNum(dateEnd[dateEnd.length - 1])
          dateEnd[dateEnd.length - 3] = this.faNumToEnNum(dateEnd[dateEnd.length - 3])

          switch (dateEnd[dateEnd.length - 2]) {
            case "فروردین":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-01-" + dateEnd[dateEnd.length - 3]
              break
            case "اردیبهشت":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-02-" + dateEnd[dateEnd.length - 3]
              break
            case "خرداد":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-03-" + dateEnd[dateEnd.length - 3]
              break
            case "تیر":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-04-" + dateEnd[dateEnd.length - 3]
              break
            case "مرداد":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-05-" + dateEnd[dateEnd.length - 3]
              break
            case "شهریور":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-06-" + dateEnd[dateEnd.length - 3]
              break
            case "مهر":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-07-" + dateEnd[dateEnd.length - 3]
              break
            case "آبان":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-08-" + dateEnd[dateEnd.length - 3]
              break
            case "آذر":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-09-" + dateEnd[dateEnd.length - 3]
              break
            case "دی":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-10-" + dateEnd[dateEnd.length - 3]
              break
            case "بهمن":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-11-" + dateEnd[dateEnd.length - 3]
              break
            case "اسفند":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-12-" + dateEnd[dateEnd.length - 3]
              break
            default:
              console.log("Wrong Input for Month")
          }

          let timeStart = dateStartTemp[1].split(":")
          let timeEnd = dateEndTemp[1].split(":")

          timeStart = this.faNumToEnNum(timeStart[0]) + ":" + this.faNumToEnNum(timeStart[1])
          timeEnd = this.faNumToEnNum(timeEnd[0]) + ":" + this.faNumToEnNum(timeEnd[1])

          const dateS = dateStartFinal.split("-")
          const dateE = dateEndFinal.split("-")

          if (parseInt(dateS[1]) < 7) {
            timeStart = timeStart + ":00.000+04:30"
          } else {
            timeStart = timeStart + ":00.000+03:30"
          }

          if (parseInt(dateE[1]) < 7) {
            timeEnd = timeEnd + ":00.000+04:30"
          } else {
            timeEnd = timeEnd + ":00.000+03:30"
          }

          let TempS = timeStart.split(":")
          let TempE = timeEnd.split(":")
          if (TempS[0].length === 1) {
            TempS[0] = "0" + TempS[0]
            timeStart = ""
            for (let i = 0; i < TempS.length; ++i) {
              timeStart = timeStart + TempS[i] + ":"
            }
            timeStart = timeStart.substring(0, timeStart.length - 1)
          }
          if (TempE[0].length === 1) {
            TempE[0] = "0" + TempE[0]
            timeEnd = ""
            for (let i = 0; i < TempE.length; ++i) {
              timeEnd = timeEnd + TempE[i] + ":"
            }
            timeEnd = timeEnd.substring(0, timeEnd.length - 1)
          }

          TempS = dateStartFinal.split("-")
          TempE = dateEndFinal.split("-")
          if (TempS[1].length === 1) {
            TempS[1] = "0" + TempS[1]
          }
          if (TempS[2].length === 1) {
            TempS[2] = "0" + TempS[2]
          }
          if (TempE[1].length === 1) {
            TempE[1] = "0" + TempE[1]
          }
          if (TempE[2].length === 1) {
            TempE[2] = "0" + TempE[2]
          }

          dateStartFinal = TempS[0] + "-" + TempS[1] + "-" + TempS[2]
          dateEndFinal = TempE[0] + "-" + TempE[1] + "-" + TempE[2]

          this.createServiceBuffer.accessStrategy.startingDateTime = dateStartFinal + "T" + timeStart
          this.createServiceBuffer.accessStrategy.endingDateTime = dateEndFinal + "T" + timeEnd
        }
        for (const i in this.createServiceBuffer.attributeList) {
          if (this.createServiceBuffer.attributeList[i].name !== "" && this.createServiceBuffer.attributeList[i].value !== "") {
            this.createServiceBuffer.accessStrategy.requiredAttributes[this.createServiceBuffer.attributeList[i].name] =
            ["java.util.HashSet", this.createServiceBuffer.attributeList[i].value.split(",")]
          }
        }
        this.createServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders = this.createServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders.value

        let serviceType = ""
        if (this.createServiceBuffer.serviceType === "CAS") {
          serviceType = "cas"
          serviceData = JSON.stringify({
            name: this.createServiceBuffer.name,
            serviceId: this.createServiceBuffer.serviceId,
            extraInfo: this.createServiceBuffer.extraInfo,
            multifactorPolicy: this.createServiceBuffer.multifactorPolicy,
            description: this.createServiceBuffer.description,
            logo: this.createServiceBuffer.logo,
            informationUrl: this.createServiceBuffer.informationUrl,
            privacyUrl: this.createServiceBuffer.privacyUrl,
            logoutType: this.createServiceBuffer.logoutType,
            logoutUrl: this.createServiceBuffer.logoutUrl,
            accessStrategy: this.createServiceBuffer.accessStrategy,
            contacts: this.createServiceBuffer.contacts
          }).replace(/\\\\/g, "\\")
        } else if (this.createServiceBuffer.serviceType === "SAML") {
          serviceType = "saml"
          serviceData = JSON.stringify({
            name: this.createServiceBuffer.name,
            serviceId: this.createServiceBuffer.serviceId,
            extraInfo: this.createServiceBuffer.extraInfo,
            metadataLocation: this.createServiceBuffer.metadataLocation,
            multifactorPolicy: this.createServiceBuffer.multifactorPolicy,
            description: this.createServiceBuffer.description,
            logo: this.createServiceBuffer.logo,
            informationUrl: this.createServiceBuffer.informationUrl,
            privacyUrl: this.createServiceBuffer.privacyUrl,
            logoutType: this.createServiceBuffer.logoutType,
            logoutUrl: this.createServiceBuffer.logoutUrl,
            accessStrategy: this.createServiceBuffer.accessStrategy,
            contacts: this.createServiceBuffer.contacts
          }).replace(/\\\\/g, "\\")
        } else if (this.createServiceBuffer.serviceType === "Oauth2") {
          serviceType = "oauth"
          serviceData = JSON.stringify({
            name: this.createServiceBuffer.name,
            serviceId: this.createServiceBuffer.serviceId,
            clientId: this.createServiceBuffer.clientId,
            clientSecret: this.createServiceBuffer.clientSecret,
            supportedGrantTypes: this.createServiceBuffer.supportedGrantTypes,
            supportedResponseTypes: this.createServiceBuffer.supportedResponseTypes,
            extraInfo: this.createServiceBuffer.extraInfo,
            metadataLocation: this.createServiceBuffer.metadataLocation,
            multifactorPolicy: this.createServiceBuffer.multifactorPolicy,
            description: this.createServiceBuffer.description,
            logo: this.createServiceBuffer.logo,
            informationUrl: this.createServiceBuffer.informationUrl,
            privacyUrl: this.createServiceBuffer.privacyUrl,
            logoutType: this.createServiceBuffer.logoutType,
            logoutUrl: this.createServiceBuffer.logoutUrl,
            accessStrategy: this.createServiceBuffer.accessStrategy,
            contacts: this.createServiceBuffer.contacts
          }).replace(/\\\\/g, "\\")
        }
        this.axios({
          url: "/api/services/" + serviceType,
          method: "POST",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: serviceData
        }).then((res) => {
          if (res.data.status.code === 201) {
            vm.createServiceLoader = false
            vm.resetState("createService")
            vm.servicesRequestMaster("getServices")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createServiceLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.createServiceLoader = false
        })
      } else if (command === "editService") {
        let serviceData = {}
        this.editServiceLoader = true
        const ouTemp = []
        if (typeof this.editServiceBuffer.accessStrategy.requiredAttributes.ou[1] !== "undefined") {
          for (const i in this.editServiceBuffer.accessStrategy.requiredAttributes.ou[1]) {
            ouTemp.push(this.editServiceBuffer.accessStrategy.requiredAttributes.ou[1][i].id)
          }
          this.editServiceBuffer.accessStrategy.requiredAttributes.ou[1] = ouTemp
        }
        const userAddTemp = []
        if (typeof this.editServiceBuffer.accessStrategy.requiredAttributes.uid[1] !== "undefined") {
          for (const i in this.editServiceBuffer.accessStrategy.requiredAttributes.uid[1]) {
            userAddTemp.push(this.editServiceBuffer.accessStrategy.requiredAttributes.uid[1][i]._id)
          }
          this.editServiceBuffer.accessStrategy.requiredAttributes.uid[1] = userAddTemp
        }
        const userBanTemp = []
        if (typeof this.editServiceBuffer.accessStrategy.rejectedAttributes.uid[1] !== "undefined") {
          for (const i in this.editServiceBuffer.accessStrategy.rejectedAttributes.uid[1]) {
            userBanTemp.push(this.editServiceBuffer.accessStrategy.rejectedAttributes.uid[1][i]._id)
          }
          this.editServiceBuffer.accessStrategy.rejectedAttributes.uid[1] = userBanTemp
        }
        const dailyAccessTemp = []
        for (let i = 0; i < 7; ++i) {
          if (document.getElementById("editSelect" + i).checked) {
            const tempDailyAccessStart = document.getElementById("editServiceBuffer.dailyAccess.start" + i).value.split(":")
            const tempDailyAccessEnd = document.getElementById("editServiceBuffer.dailyAccess.end" + i).value.split(":")
            if (tempDailyAccessStart[0] === "") {
              tempDailyAccessStart[0] = "0"
              tempDailyAccessStart.push("00")
            } else if (typeof tempDailyAccessStart[1] === "undefined") {
              tempDailyAccessStart.push("00")
            }
            if (tempDailyAccessEnd[0] === "") {
              tempDailyAccessEnd[0] = "23"
              tempDailyAccessEnd.push("59")
            } else if (typeof tempDailyAccessEnd[1] === "undefined") {
              tempDailyAccessEnd.push("00")
            }
            tempDailyAccessStart[0] = this.faNumToEnNum(tempDailyAccessStart[0])
            tempDailyAccessStart[1] = this.faNumToEnNum(tempDailyAccessStart[1])
            tempDailyAccessEnd[0] = this.faNumToEnNum(tempDailyAccessEnd[0])
            tempDailyAccessEnd[1] = this.faNumToEnNum(tempDailyAccessEnd[1])
            const tempDailyAccessDay = {
              weekDay: i,
              period: {
                from: {
                  hour: tempDailyAccessStart[0],
                  minute: tempDailyAccessStart[1]
                },
                to: {
                  hour: tempDailyAccessEnd[0],
                  minute: tempDailyAccessEnd[1]
                }
              }
            }
            dailyAccessTemp.push(tempDailyAccessDay)
          }
        }
        if (dailyAccessTemp.length > 0) {
          this.editServiceBuffer.extraInfo.dailyAccess = dailyAccessTemp
        }
        if (document.getElementById("editServiceBuffer.startDate").value !== "" && document.getElementById("editServiceBuffer.endDate").value !== "") {
          const dateStartTemp = document.getElementById("editServiceBuffer.startDate").value.split("  ")
          const dateStart = dateStartTemp[0].split(" ")
          let dateStartFinal
          dateStart[dateStart.length - 1] = this.faNumToEnNum(dateStart[dateStart.length - 1])
          dateStart[dateStart.length - 3] = this.faNumToEnNum(dateStart[dateStart.length - 3])

          switch (dateStart[dateStart.length - 2]) {
            case "فروردین":
              dateStartFinal = dateStart[dateStart.length - 1] + "-01-" + dateStart[dateStart.length - 3]
              break
            case "اردیبهشت":
              dateStartFinal = dateStart[dateStart.length - 1] + "-02-" + dateStart[dateStart.length - 3]
              break
            case "خرداد":
              dateStartFinal = dateStart[dateStart.length - 1] + "-03-" + dateStart[dateStart.length - 3]
              break
            case "تیر":
              dateStartFinal = dateStart[dateStart.length - 1] + "-04-" + dateStart[dateStart.length - 3]
              break
            case "مرداد":
              dateStartFinal = dateStart[dateStart.length - 1] + "-05-" + dateStart[dateStart.length - 3]
              break
            case "شهریور":
              dateStartFinal = dateStart[dateStart.length - 1] + "-06-" + dateStart[dateStart.length - 3]
              break
            case "مهر":
              dateStartFinal = dateStart[dateStart.length - 1] + "-07-" + dateStart[dateStart.length - 3]
              break
            case "آبان":
              dateStartFinal = dateStart[dateStart.length - 1] + "-08-" + dateStart[dateStart.length - 3]
              break
            case "آذر":
              dateStartFinal = dateStart[dateStart.length - 1] + "-09-" + dateStart[dateStart.length - 3]
              break
            case "دی":
              dateStartFinal = dateStart[dateStart.length - 1] + "-10-" + dateStart[dateStart.length - 3]
              break
            case "بهمن":
              dateStartFinal = dateStart[dateStart.length - 1] + "-11-" + dateStart[dateStart.length - 3]
              break
            case "اسفند":
              dateStartFinal = dateStart[dateStart.length - 1] + "-12-" + dateStart[dateStart.length - 3]
              break
            default:
              console.log("Wrong Input for Month")
          }

          const dateEndTemp = document.getElementById("editServiceBuffer.endDate").value.split("  ")
          const dateEnd = dateEndTemp[0].split(" ")
          let dateEndFinal
          dateEnd[dateEnd.length - 1] = this.faNumToEnNum(dateEnd[dateEnd.length - 1])
          dateEnd[dateEnd.length - 3] = this.faNumToEnNum(dateEnd[dateEnd.length - 3])

          switch (dateEnd[dateEnd.length - 2]) {
            case "فروردین":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-01-" + dateEnd[dateEnd.length - 3]
              break
            case "اردیبهشت":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-02-" + dateEnd[dateEnd.length - 3]
              break
            case "خرداد":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-03-" + dateEnd[dateEnd.length - 3]
              break
            case "تیر":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-04-" + dateEnd[dateEnd.length - 3]
              break
            case "مرداد":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-05-" + dateEnd[dateEnd.length - 3]
              break
            case "شهریور":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-06-" + dateEnd[dateEnd.length - 3]
              break
            case "مهر":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-07-" + dateEnd[dateEnd.length - 3]
              break
            case "آبان":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-08-" + dateEnd[dateEnd.length - 3]
              break
            case "آذر":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-09-" + dateEnd[dateEnd.length - 3]
              break
            case "دی":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-10-" + dateEnd[dateEnd.length - 3]
              break
            case "بهمن":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-11-" + dateEnd[dateEnd.length - 3]
              break
            case "اسفند":
              dateEndFinal = dateEnd[dateEnd.length - 1] + "-12-" + dateEnd[dateEnd.length - 3]
              break
            default:
              console.log("Wrong Input for Month")
          }

          let timeStart = dateStartTemp[1].split(":")
          let timeEnd = dateEndTemp[1].split(":")

          timeStart = this.faNumToEnNum(timeStart[0]) + ":" + this.faNumToEnNum(timeStart[1])
          timeEnd = this.faNumToEnNum(timeEnd[0]) + ":" + this.faNumToEnNum(timeEnd[1])

          const dateS = dateStartFinal.split("-")
          const dateE = dateEndFinal.split("-")

          if (parseInt(dateS[1]) < 7) {
            timeStart = timeStart + ":00.000+04:30"
          } else {
            timeStart = timeStart + ":00.000+03:30"
          }

          if (parseInt(dateE[1]) < 7) {
            timeEnd = timeEnd + ":00.000+04:30"
          } else {
            timeEnd = timeEnd + ":00.000+03:30"
          }

          let TempS = timeStart.split(":")
          let TempE = timeEnd.split(":")
          if (TempS[0].length === 1) {
            TempS[0] = "0" + TempS[0]
            timeStart = ""
            for (let i = 0; i < TempS.length; ++i) {
              timeStart = timeStart + TempS[i] + ":"
            }
            timeStart = timeStart.substring(0, timeStart.length - 1)
          }
          if (TempE[0].length === 1) {
            TempE[0] = "0" + TempE[0]
            timeEnd = ""
            for (let i = 0; i < TempE.length; ++i) {
              timeEnd = timeEnd + TempE[i] + ":"
            }
            timeEnd = timeEnd.substring(0, timeEnd.length - 1)
          }

          TempS = dateStartFinal.split("-")
          TempE = dateEndFinal.split("-")
          if (TempS[1].length === 1) {
            TempS[1] = "0" + TempS[1]
          }
          if (TempS[2].length === 1) {
            TempS[2] = "0" + TempS[2]
          }
          if (TempE[1].length === 1) {
            TempE[1] = "0" + TempE[1]
          }
          if (TempE[2].length === 1) {
            TempE[2] = "0" + TempE[2]
          }

          dateStartFinal = TempS[0] + "-" + TempS[1] + "-" + TempS[2]
          dateEndFinal = TempE[0] + "-" + TempE[1] + "-" + TempE[2]

          this.editServiceBuffer.accessStrategy.startingDateTime = dateStartFinal + "T" + timeStart
          this.editServiceBuffer.accessStrategy.endingDateTime = dateEndFinal + "T" + timeEnd
        }
        for (const i in this.editServiceBuffer.attributeList) {
          if (this.editServiceBuffer.attributeList[i].name !== "" && this.editServiceBuffer.attributeList[i].value !== "") {
            this.editServiceBuffer.accessStrategy.requiredAttributes[this.editServiceBuffer.attributeList[i].name] =
            ["java.util.HashSet", this.editServiceBuffer.attributeList[i].value.split(",")]
          }
        }
        this.editServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders = this.editServiceBuffer.multifactorPolicy.multifactorAuthenticationProviders.value

        let serviceType = ""
        if (this.editServiceBuffer.serviceType === "CAS") {
          serviceType = "cas"
          serviceData = JSON.stringify({
            name: this.editServiceBuffer.name,
            serviceId: this.editServiceBuffer.serviceId,
            extraInfo: this.editServiceBuffer.extraInfo,
            multifactorPolicy: this.editServiceBuffer.multifactorPolicy,
            description: this.editServiceBuffer.description,
            logo: this.editServiceBuffer.logo,
            informationUrl: this.editServiceBuffer.informationUrl,
            privacyUrl: this.editServiceBuffer.privacyUrl,
            logoutType: this.editServiceBuffer.logoutType,
            logoutUrl: this.editServiceBuffer.logoutUrl,
            accessStrategy: this.editServiceBuffer.accessStrategy,
            contacts: this.editServiceBuffer.contacts
          }).replace(/\\\\/g, "\\")
        } else if (this.editServiceBuffer.serviceType === "SAML") {
          serviceType = "saml"
          serviceData = JSON.stringify({
            name: this.editServiceBuffer.name,
            serviceId: this.editServiceBuffer.serviceId,
            extraInfo: this.editServiceBuffer.extraInfo,
            metadataLocation: this.editServiceBuffer.metadataLocation,
            multifactorPolicy: this.editServiceBuffer.multifactorPolicy,
            description: this.editServiceBuffer.description,
            logo: this.editServiceBuffer.logo,
            informationUrl: this.editServiceBuffer.informationUrl,
            privacyUrl: this.editServiceBuffer.privacyUrl,
            logoutType: this.editServiceBuffer.logoutType,
            logoutUrl: this.editServiceBuffer.logoutUrl,
            accessStrategy: this.editServiceBuffer.accessStrategy,
            contacts: this.editServiceBuffer.contacts
          }).replace(/\\\\/g, "\\")
        } else if (this.editServiceBuffer.serviceType === "Oauth2") {
          serviceType = "oauth"
          serviceData = JSON.stringify({
            name: this.editServiceBuffer.name,
            serviceId: this.editServiceBuffer.serviceId,
            clientId: this.editServiceBuffer.clientId,
            clientSecret: this.editServiceBuffer.clientSecret,
            supportedGrantTypes: this.editServiceBuffer.supportedGrantTypes,
            supportedResponseTypes: this.editServiceBuffer.supportedResponseTypes,
            extraInfo: this.editServiceBuffer.extraInfo,
            metadataLocation: this.editServiceBuffer.metadataLocation,
            multifactorPolicy: this.editServiceBuffer.multifactorPolicy,
            description: this.editServiceBuffer.description,
            logo: this.editServiceBuffer.logo,
            informationUrl: this.editServiceBuffer.informationUrl,
            privacyUrl: this.editServiceBuffer.privacyUrl,
            logoutType: this.editServiceBuffer.logoutType,
            logoutUrl: this.editServiceBuffer.logoutUrl,
            accessStrategy: this.editServiceBuffer.accessStrategy,
            contacts: this.editServiceBuffer.contacts
          }).replace(/\\\\/g, "\\")
        }
        this.axios({
          url: "/api/service/" + vm.editServiceBuffer.id + "/" + serviceType,
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: serviceData
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.editServiceLoader = false
            vm.resetState("editService")
            vm.servicesRequestMaster("getServices")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editServiceLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.editServiceLoader = false
        })
      } else if (command === "deleteService") {
        const selectedServiceList = [parseInt(this.serviceToolbarBuffer)]
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
          selectedServicesList.push(parseInt(this.selectedServices[x]._id))
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
      } else if (command === "initiateGroups") {
        this.createServiceLoader = true
        this.axios({
          url: "/api/groups",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          vm.groups = res.data.data
          vm.createServiceLoader = false
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.createServiceLoader = false
        })
      } else if (command === "initiateUsers") {
        this.createServiceUsersLoader = true
        this.axios({
          url: "/api/users",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.users = res.data.data.userList
            vm.createUsers = res.data.data.userList
            vm.createServiceUsersLoader = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createServiceUsersLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.createServiceUsersLoader = false
        })
      } else if (command === "initiateGroupsEdit") {
        this.editServiceLoader = true
        this.axios({
          url: "/api/groups",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          vm.groups = res.data.data
          vm.editServiceLoader = false
          vm.servicesRequestMaster("initiateUsersEdit")
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.editServiceLoader = false
        })
      } else if (command === "initiateUsersEdit") {
        this.editServiceUsersLoader = true
        this.axios({
          url: "/api/users",
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.users = res.data.data.userList
            vm.editUsers = res.data.data.userList
            vm.editServiceUsersLoader = false
            vm.servicesRequestMaster("getService")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editServiceUsersLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.editServiceUsersLoader = false
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
      this.servicesRequestMaster("initiateGroups")
      this.servicesRequestMaster("initiateUsers")
    },
    createServiceCheckup () {
      const emailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      let errorCount = 0
      if (this.createServiceBuffer.name === "") {
        this.createServiceErrors.name = "p-invalid"
        errorCount += 1
      } else {
        this.createServiceErrors.name = ""
      }
      if (this.createServiceBuffer.serviceId === "") {
        this.createServiceErrors.serviceId = "p-invalid"
        errorCount += 1
      } else {
        for (let i = 0; i < this.createServiceBuffer.serviceId.length; ++i) {
          if (i === 0) {
            if (this.createServiceBuffer.serviceId[i] === "\\" &&
            this.createServiceBuffer.serviceId[i + 1] !== "\\") {
              this.createServiceErrors.serviceId = "p-invalid"
              errorCount += 1
              break
            } else {
              this.createServiceErrors.serviceId = ""
            }
          } else if (i === this.createServiceBuffer.serviceId.length - 1) {
            if (this.createServiceBuffer.serviceId[i] === "\\" &&
            this.createServiceBuffer.serviceId[i - 1] !== "\\") {
              this.createServiceErrors.serviceId = "p-invalid"
              errorCount += 1
              break
            } else {
              this.createServiceErrors.serviceId = ""
            }
          } else {
            if (this.createServiceBuffer.serviceId[i] === "\\" &&
            this.createServiceBuffer.serviceId[i + 1] !== "\\" &&
            this.createServiceBuffer.serviceId[i - 1] !== "\\") {
              this.createServiceErrors.serviceId = "p-invalid"
              errorCount += 1
              break
            } else {
              this.createServiceErrors.serviceId = ""
            }
          }
        }
      }
      if (this.createServiceBuffer.description === "") {
        this.createServiceErrors.description = "p-invalid"
        errorCount += 1
      } else {
        this.createServiceErrors.description = ""
      }
      if (this.createServiceBuffer.extraInfo.url === "") {
        this.createServiceErrors.extraInfo.url = "p-invalid"
        errorCount += 1
      } else {
        this.createServiceErrors.extraInfo.url = ""
      }
      if (this.createServiceBuffer.contacts[1][0].name === "") {
        this.createServiceErrors.contacts.name = "p-invalid"
        errorCount += 1
      } else {
        this.createServiceErrors.contacts.name = ""
      }
      if (this.createServiceBuffer.contacts[1][0].email === "") {
        this.createServiceErrors.contacts.email = "p-invalid"
        errorCount += 1
      } else {
        if (!emailRegex.test(this.createServiceBuffer.contacts[1][0].email)) {
          this.createServiceErrors.contacts.email = "p-invalid"
          errorCount += 1
        } else {
          this.createServiceErrors.contacts.email = ""
        }
      }

      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        if (this.createServiceBuffer.logoFile !== null) {
          const vm = this
          const logoFileBodyFormData = new FormData()
          logoFileBodyFormData.append("file", vm.createServiceBuffer.logoFile)
          vm.createServiceLoader = true
          vm.axios({
            url: "/api/services/icon",
            method: "POST",
            headers: { "Content-Type": "multipart/form-data" },
            data: logoFileBodyFormData
          }).then((res) => {
            if (res.data.status.code === 200) {
              vm.createServiceBuffer.logo = res.data.data
            }
            vm.createServiceLoader = false

            if (vm.createServiceBuffer.serviceType === "SAML") {
              if (vm.createServiceBuffer.metadataLocationOption === "address") {
                if (vm.createServiceBuffer.metadataLocation === "") {
                  vm.createServiceErrors.metadataLocation = "p-invalid"
                  errorCount += 1
                } else {
                  vm.createServiceErrors.metadataLocation = ""
                }

                if (errorCount > 0) {
                  vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
                } else {
                  vm.servicesRequestMaster("createService")
                }
              } else if (vm.createServiceBuffer.metadataLocationOption === "file") {
                const metadataFileBodyFormData = new FormData()
                metadataFileBodyFormData.append("file", vm.createServiceBuffer.metadataFile)
                vm.createServiceLoader = true
                vm.axios({
                  url: "/api/services/metadata",
                  method: "POST",
                  headers: { "Content-Type": "multipart/form-data" },
                  data: metadataFileBodyFormData
                }).then((res) => {
                  if (res.data.status.code === 200) {
                    vm.createServiceErrors.metadataLocation = ""
                    vm.createServiceBuffer.metadataLocation = res.data.data
                  } else {
                    vm.createServiceErrors.metadataLocation = "p-invalid"
                    errorCount += 1
                  }
                  vm.createServiceLoader = false

                  if (errorCount > 0) {
                    vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
                  } else {
                    vm.servicesRequestMaster("createService")
                  }
                }).catch(() => {
                  vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                  vm.createServiceErrors.metadataLocation = "p-invalid"
                  vm.createServiceLoader = false
                })
              }
            } else if (vm.createServiceBuffer.serviceType === "Oauth2") {
              if (vm.createServiceBuffer.clientId === "") {
                vm.createServiceErrors.clientId = "p-invalid"
                errorCount += 1
              } else {
                vm.createServiceErrors.clientId = ""
              }
              if (vm.createServiceBuffer.clientSecret === "") {
                vm.createServiceErrors.clientSecret = "p-invalid"
                errorCount += 1
              } else {
                vm.createServiceErrors.clientSecret = ""
              }
              if (vm.createServiceBuffer.supportedGrantTypes[1].length === 0) {
                vm.createServiceErrors.supportedGrantTypes = "p-invalid"
                errorCount += 1
              } else {
                vm.createServiceErrors.supportedGrantTypes = ""
              }
              if (vm.createServiceBuffer.supportedResponseTypes[1].length === 0) {
                vm.createServiceErrors.supportedResponseTypes = "p-invalid"
                errorCount += 1
              } else {
                vm.createServiceErrors.supportedResponseTypes = ""
              }

              if (errorCount > 0) {
                vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
              } else {
                vm.servicesRequestMaster("createService")
              }
            } else {
              this.servicesRequestMaster("createService")
            }
          }).catch(() => {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createServiceLoader = false
          })
        } else {
          if (this.createServiceBuffer.serviceType === "SAML") {
            if (this.createServiceBuffer.metadataLocationOption === "address") {
              if (this.createServiceBuffer.metadataLocation === "") {
                this.createServiceErrors.metadataLocation = "p-invalid"
                errorCount += 1
              } else {
                this.createServiceErrors.metadataLocation = ""
              }

              if (errorCount > 0) {
                this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
              } else {
                this.servicesRequestMaster("createService")
              }
            } else if (this.createServiceBuffer.metadataLocationOption === "file") {
              const vm = this
              const metadataFileBodyFormData = new FormData()
              metadataFileBodyFormData.append("file", this.createServiceBuffer.metadataFile)
              this.createServiceLoader = true
              this.axios({
                url: "/api/services/metadata",
                method: "POST",
                headers: { "Content-Type": "multipart/form-data" },
                data: metadataFileBodyFormData
              }).then((res) => {
                if (res.data.status.code === 200) {
                  vm.createServiceErrors.metadataLocation = ""
                  vm.createServiceBuffer.metadataLocation = res.data.data
                } else {
                  vm.createServiceErrors.metadataLocation = "p-invalid"
                  errorCount += 1
                }
                vm.createServiceLoader = false

                if (errorCount > 0) {
                  vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
                } else {
                  vm.servicesRequestMaster("createService")
                }
              }).catch(() => {
                vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                vm.createServiceErrors.metadataLocation = "p-invalid"
                vm.createServiceLoader = false
              })
            }
          } else if (this.createServiceBuffer.serviceType === "Oauth2") {
            if (this.createServiceBuffer.clientId === "") {
              this.createServiceErrors.clientId = "p-invalid"
              errorCount += 1
            } else {
              this.createServiceErrors.clientId = ""
            }
            if (this.createServiceBuffer.clientSecret === "") {
              this.createServiceErrors.clientSecret = "p-invalid"
              errorCount += 1
            } else {
              this.createServiceErrors.clientSecret = ""
            }
            if (this.createServiceBuffer.supportedGrantTypes[1].length === 0) {
              this.createServiceErrors.supportedGrantTypes = "p-invalid"
              errorCount += 1
            } else {
              this.createServiceErrors.supportedGrantTypes = ""
            }
            if (this.createServiceBuffer.supportedResponseTypes[1].length === 0) {
              this.createServiceErrors.supportedResponseTypes = "p-invalid"
              errorCount += 1
            } else {
              this.createServiceErrors.supportedResponseTypes = ""
            }

            if (errorCount > 0) {
              this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
            } else {
              this.servicesRequestMaster("createService")
            }
          } else {
            this.servicesRequestMaster("createService")
          }
        }
      }
    },
    editService (id) {
      this.editServiceBuffer.id = id
      this.editServiceFlag = true
      if (this.createServiceFlag) {
        this.tabActiveIndexList = 2
      } else {
        this.tabActiveIndexList = 1
      }
      this.servicesRequestMaster("initiateGroupsEdit")
    },
    editServiceCheckup () {
      const emailRegex = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/
      let errorCount = 0
      if (this.editServiceBuffer.name === "") {
        this.editServiceErrors.name = "p-invalid"
        errorCount += 1
      } else {
        this.editServiceErrors.name = ""
      }
      if (this.editServiceBuffer.serviceId === "") {
        this.editServiceErrors.serviceId = "p-invalid"
        errorCount += 1
      } else {
        for (let i = 0; i < this.editServiceBuffer.serviceId.length; ++i) {
          if (i === 0) {
            if (this.editServiceBuffer.serviceId[i] === "\\" &&
            this.editServiceBuffer.serviceId[i + 1] !== "\\") {
              this.editServiceErrors.serviceId = "p-invalid"
              errorCount += 1
              break
            } else {
              this.editServiceErrors.serviceId = ""
            }
          } else if (i === this.editServiceBuffer.serviceId.length - 1) {
            if (this.editServiceBuffer.serviceId[i] === "\\" &&
            this.editServiceBuffer.serviceId[i - 1] !== "\\") {
              this.editServiceErrors.serviceId = "p-invalid"
              errorCount += 1
              break
            } else {
              this.editServiceErrors.serviceId = ""
            }
          } else {
            if (this.editServiceBuffer.serviceId[i] === "\\" &&
            this.editServiceBuffer.serviceId[i + 1] !== "\\" &&
            this.editServiceBuffer.serviceId[i - 1] !== "\\") {
              this.editServiceErrors.serviceId = "p-invalid"
              errorCount += 1
              break
            } else {
              this.editServiceErrors.serviceId = ""
            }
          }
        }
      }
      if (this.editServiceBuffer.description === "") {
        this.editServiceErrors.description = "p-invalid"
        errorCount += 1
      } else {
        this.editServiceErrors.description = ""
      }
      if (this.editServiceBuffer.extraInfo.url === "") {
        this.editServiceErrors.extraInfo.url = "p-invalid"
        errorCount += 1
      } else {
        this.editServiceErrors.extraInfo.url = ""
      }
      if (this.editServiceBuffer.contacts[1][0].name === "") {
        this.editServiceErrors.contacts.name = "p-invalid"
        errorCount += 1
      } else {
        this.editServiceErrors.contacts.name = ""
      }
      if (this.editServiceBuffer.contacts[1][0].email === "") {
        this.editServiceErrors.contacts.email = "p-invalid"
        errorCount += 1
      } else {
        if (!emailRegex.test(this.editServiceBuffer.contacts[1][0].email)) {
          this.editServiceErrors.contacts.email = "p-invalid"
          errorCount += 1
        } else {
          this.editServiceErrors.contacts.email = ""
        }
      }

      if (errorCount > 0) {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        if (this.editServiceBuffer.logoFile !== null) {
          const vm = this
          const logoFileBodyFormData = new FormData()
          logoFileBodyFormData.append("file", vm.editServiceBuffer.logoFile)
          vm.editServiceLoader = true
          vm.axios({
            url: "/api/services/icon",
            method: "POST",
            headers: { "Content-Type": "multipart/form-data" },
            data: logoFileBodyFormData
          }).then((res) => {
            if (res.data.status.code === 200) {
              vm.editServiceBuffer.logo = res.data.data
            }
            vm.editServiceLoader = false

            if (vm.editServiceBuffer.serviceType === "SAML") {
              if (vm.editServiceBuffer.metadataLocationOption === "address") {
                if (vm.editServiceBuffer.metadataLocation === "") {
                  vm.editServiceErrors.metadataLocation = "p-invalid"
                  errorCount += 1
                } else {
                  vm.editServiceErrors.metadataLocation = ""
                }

                if (errorCount > 0) {
                  vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
                } else {
                  vm.servicesRequestMaster("editService")
                }
              } else if (vm.editServiceBuffer.metadataLocationOption === "file") {
                const metadataFileBodyFormData = new FormData()
                metadataFileBodyFormData.append("file", vm.editServiceBuffer.metadataFile)
                vm.editServiceLoader = true
                vm.axios({
                  url: "/api/services/metadata",
                  method: "POST",
                  headers: { "Content-Type": "multipart/form-data" },
                  data: metadataFileBodyFormData
                }).then((res) => {
                  if (res.data.status.code === 200) {
                    vm.editServiceErrors.metadataLocation = ""
                    vm.editServiceBuffer.metadataLocation = res.data.data
                  } else {
                    vm.editServiceErrors.metadataLocation = "p-invalid"
                    errorCount += 1
                  }
                  vm.editServiceLoader = false

                  if (errorCount > 0) {
                    vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
                  } else {
                    vm.servicesRequestMaster("editService")
                  }
                }).catch(() => {
                  vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                  vm.editServiceErrors.metadataLocation = "p-invalid"
                  vm.editServiceLoader = false
                })
              }
            } else if (vm.editServiceBuffer.serviceType === "Oauth2") {
              if (vm.editServiceBuffer.clientId === "") {
                vm.editServiceErrors.clientId = "p-invalid"
                errorCount += 1
              } else {
                vm.editServiceErrors.clientId = ""
              }
              if (vm.editServiceBuffer.clientSecret === "") {
                vm.editServiceErrors.clientSecret = "p-invalid"
                errorCount += 1
              } else {
                vm.editServiceErrors.clientSecret = ""
              }
              if (vm.editServiceBuffer.supportedGrantTypes[1].length === 0) {
                vm.editServiceErrors.supportedGrantTypes = "p-invalid"
                errorCount += 1
              } else {
                vm.editServiceErrors.supportedGrantTypes = ""
              }
              if (vm.editServiceBuffer.supportedResponseTypes[1].length === 0) {
                vm.editServiceErrors.supportedResponseTypes = "p-invalid"
                errorCount += 1
              } else {
                vm.editServiceErrors.supportedResponseTypes = ""
              }

              if (errorCount > 0) {
                vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
              } else {
                vm.servicesRequestMaster("editService")
              }
            } else {
              this.servicesRequestMaster("editService")
            }
          }).catch(() => {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editServiceLoader = false
          })
        } else {
          if (this.editServiceBuffer.serviceType === "SAML") {
            if (this.editServiceBuffer.metadataLocationOption === "address") {
              if (this.editServiceBuffer.metadataLocation === "") {
                this.editServiceErrors.metadataLocation = "p-invalid"
                errorCount += 1
              } else {
                this.editServiceErrors.metadataLocation = ""
              }

              if (errorCount > 0) {
                this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
              } else {
                this.servicesRequestMaster("editService")
              }
            } else if (this.editServiceBuffer.metadataLocationOption === "file") {
              const vm = this
              const metadataFileBodyFormData = new FormData()
              metadataFileBodyFormData.append("file", this.editServiceBuffer.metadataFile)
              this.editServiceLoader = true
              this.axios({
                url: "/api/services/metadata",
                method: "POST",
                headers: { "Content-Type": "multipart/form-data" },
                data: metadataFileBodyFormData
              }).then((res) => {
                if (res.data.status.code === 200) {
                  vm.editServiceErrors.metadataLocation = ""
                  vm.editServiceBuffer.metadataLocation = res.data.data
                } else {
                  vm.editServiceErrors.metadataLocation = "p-invalid"
                  errorCount += 1
                }
                vm.editServiceLoader = false

                if (errorCount > 0) {
                  vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
                } else {
                  vm.servicesRequestMaster("editService")
                }
              }).catch(() => {
                vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                vm.editServiceErrors.metadataLocation = "p-invalid"
                vm.editServiceLoader = false
              })
            }
          } else if (this.editServiceBuffer.serviceType === "Oauth2") {
            if (this.editServiceBuffer.clientId === "") {
              this.editServiceErrors.clientId = "p-invalid"
              errorCount += 1
            } else {
              this.editServiceErrors.clientId = ""
            }
            if (this.editServiceBuffer.clientSecret === "") {
              this.editServiceErrors.clientSecret = "p-invalid"
              errorCount += 1
            } else {
              this.editServiceErrors.clientSecret = ""
            }
            if (this.editServiceBuffer.supportedGrantTypes[1].length === 0) {
              this.editServiceErrors.supportedGrantTypes = "p-invalid"
              errorCount += 1
            } else {
              this.editServiceErrors.supportedGrantTypes = ""
            }
            if (this.editServiceBuffer.supportedResponseTypes[1].length === 0) {
              this.editServiceErrors.supportedResponseTypes = "p-invalid"
              errorCount += 1
            } else {
              this.editServiceErrors.supportedResponseTypes = ""
            }

            if (errorCount > 0) {
              this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
            } else {
              this.servicesRequestMaster("editService")
            }
          } else {
            this.servicesRequestMaster("editService")
          }
        }
      }
    },
    deleteService (id, name) {
      this.serviceToolbarBuffer = id
      this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + String(name), "pi-question-circle", "#F0EAAA", this.servicesRequestMaster, "deleteService")
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
        this.tabActiveIndexList = 0
        this.createServiceFlag = false
        this.createServiceBuffer = {
          serviceType: "CAS",
          logoFile: null,
          metadataFile: null,
          dailyAccessType: {
            id: "DAY",
            name: this.$t("dayBased")
          },
          attributeList: [
            {
              name: "",
              value: ""
            }
          ],
          id: "",
          name: "",
          serviceId: "",
          description: "",
          metadataLocation: "",
          metadataLocationOption: "address",
          clientId: "",
          clientSecret: "",
          supportedGrantTypes: [
            "java.util.HashSet",
            ["authorization_code"]
          ],
          supportedResponseTypes: [
            "java.util.HashSet",
            ["code"]
          ],
          logo: null,
          informationUrl: null,
          privacyUrl: null,
          logoutType: "BACK_CHANNEL",
          logoutUrl: null,
          contacts: [
            "java.util.ArrayList",
            [
              {
                name: "",
                email: "",
                phone: null,
                department: null
              }
            ]
          ],
          accessStrategy: {
            enabled: true,
            ssoEnabled: true,
            unauthorizedRedirectUrl: null,
            endpointUrl: null,
            acceptableResponseCodes: null,
            requiredAttributes: {
              uid: [
                "java.util.HashSet",
                []
              ],
              ou: [
                "java.util.HashSet",
                []
              ]
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
            url: "",
            dailyAccess: null,
            notificationApiURL: null,
            notificationApiKey: null
          }
        }
      } else if (command === "editService") {
        this.tabActiveIndexList = 0
        this.editServiceFlag = false
        this.editServiceBuffer = {
          serviceType: "CAS",
          logoFile: null,
          metadataFile: null,
          dailyAccessType: {
            id: "DAY",
            name: this.$t("dayBased")
          },
          attributeList: [
            {
              name: "",
              value: ""
            }
          ],
          id: "",
          name: "",
          serviceId: "",
          description: "",
          metadataLocation: "",
          metadataLocationOption: "address",
          clientId: "",
          clientSecret: "",
          supportedGrantTypes: [
            "java.util.HashSet",
            ["authorization_code"]
          ],
          supportedResponseTypes: [
            "java.util.HashSet",
            ["code"]
          ],
          logo: null,
          informationUrl: null,
          privacyUrl: null,
          logoutType: "BACK_CHANNEL",
          logoutUrl: null,
          contacts: [
            "java.util.ArrayList",
            [
              {
                name: "",
                email: "",
                phone: null,
                department: null
              }
            ]
          ],
          accessStrategy: {
            enabled: true,
            ssoEnabled: true,
            unauthorizedRedirectUrl: null,
            endpointUrl: null,
            acceptableResponseCodes: null,
            requiredAttributes: {
              uid: [
                "java.util.HashSet",
                []
              ],
              ou: [
                "java.util.HashSet",
                []
              ]
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
            url: "",
            dailyAccess: null,
            notificationApiURL: null,
            notificationApiKey: null
          }
        }
      }
    },
    generateSecret (scope) {
      if (scope === "create") {
        this.createServiceBuffer.clientSecret = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15)
      } else if (scope === "edit") {
        this.editServiceBuffer.clientSecret = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15)
      }
    },
    createServiceAllUsersToggle (user, type, command) {
      if (type === "list") {
        if (command === "add") {
          this.createServiceBuffer.accessStrategy.requiredAttributes.uid[1].push(user)
        } else if (command === "remove") {
          this.createServiceBuffer.accessStrategy.rejectedAttributes.uid[1].push(user)
        }
        this.createUsers = this.createUsers.filter((item) => item._id !== user._id)
      } else if (type === "authorize") {
        this.createUsers.push(user)
        this.createServiceBuffer.accessStrategy.requiredAttributes.uid[1] = this.createServiceBuffer.accessStrategy.requiredAttributes.uid[1].filter((item) => item._id !== user._id)
      } else if (type === "ban") {
        this.createUsers.push(user)
        this.createServiceBuffer.accessStrategy.rejectedAttributes.uid[1] = this.createServiceBuffer.accessStrategy.rejectedAttributes.uid[1].filter((item) => item._id !== user._id)
      }
    },
    editServiceAllUsersToggle (user, type, command) {
      if (type === "list") {
        if (command === "add") {
          this.editServiceBuffer.accessStrategy.requiredAttributes.uid[1].push(user)
        } else if (command === "remove") {
          this.editServiceBuffer.accessStrategy.rejectedAttributes.uid[1].push(user)
        }
        this.editUsers = this.editUsers.filter((item) => item._id !== user._id)
      } else if (type === "authorize") {
        this.editUsers.push(user)
        this.editServiceBuffer.accessStrategy.requiredAttributes.uid[1] = this.editServiceBuffer.accessStrategy.requiredAttributes.uid[1].filter((item) => item._id !== user._id)
      } else if (type === "ban") {
        this.editUsers.push(user)
        this.editServiceBuffer.accessStrategy.rejectedAttributes.uid[1] = this.editServiceBuffer.accessStrategy.rejectedAttributes.uid[1].filter((item) => item._id !== user._id)
      }
    },
    attributeHelper (scope, command, index) {
      if (scope === "create") {
        if (command === "add") {
          this.createServiceBuffer.attributeList.push({ name: "", value: "" })
        } else if (command === "remove") {
          this.createServiceBuffer.attributeList.splice(index, 1)
        }
      } else if (scope === "edit") {
        if (command === "add") {
          this.editServiceBuffer.attributeList.push({ name: "", value: "" })
        } else if (command === "remove") {
          this.editServiceBuffer.attributeList.splice(index, 1)
        }
      }
    },
    fileUploadHelper (event, scope, command) {
      if (scope === "create") {
        if (command === "metadata") {
          this.createServiceBuffer.metadataFile = event.files[0]
        } else if (command === "logo") {
          this.createServiceBuffer.logoFile = event.files[0]
        }
      } else if (scope === "edit") {
        if (command === "metadata") {
          this.editServiceBuffer.metadataFile = event.files[0]
        } else if (command === "logo") {
          this.editServiceBuffer.logoFile = event.files[0]
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
    },
    faNumToEnNum (faNum) {
      const s = faNum.split("")
      let sEn = ""
      for (let i = 0; i < s.length; ++i) {
        if (s[i] === "۰") {
          sEn = sEn + "0"
        } else if (s[i] === "۱") {
          sEn = sEn + "1"
        } else if (s[i] === "۲") {
          sEn = sEn + "2"
        } else if (s[i] === "۳") {
          sEn = sEn + "3"
        } else if (s[i] === "۴") {
          sEn = sEn + "4"
        } else if (s[i] === "۵") {
          sEn = sEn + "5"
        } else if (s[i] === "۶") {
          sEn = sEn + "6"
        } else if (s[i] === "۷") {
          sEn = sEn + "7"
        } else if (s[i] === "۸") {
          sEn = sEn + "8"
        } else if (s[i] === "۹") {
          sEn = sEn + "9"
        }
      }
      return sEn
    },
    enNumToFaNum (str) {
      const s = str.split("")
      let sFa = ""
      for (let i = 0; i < s.length; ++i) {
        if (s[i] === "۰" || s[i] === "0") {
          sFa = sFa + "۰"
        } else if (s[i] === "۱" || s[i] === "1") {
          sFa = sFa + "۱"
        } else if (s[i] === "۲" || s[i] === "2") {
          sFa = sFa + "۲"
        } else if (s[i] === "۳" || s[i] === "3") {
          sFa = sFa + "۳"
        } else if (s[i] === "۴" || s[i] === "4") {
          sFa = sFa + "۴"
        } else if (s[i] === "۵" || s[i] === "5") {
          sFa = sFa + "۵"
        } else if (s[i] === "۶" || s[i] === "6") {
          sFa = sFa + "۶"
        } else if (s[i] === "۷" || s[i] === "7") {
          sFa = sFa + "۷"
        } else if (s[i] === "۸" || s[i] === "8") {
          sFa = sFa + "۸"
        } else if (s[i] === "۹" || s[i] === "9") {
          sFa = sFa + "۹"
        }
      }
      return sFa
    },
    s2ab (s) {
      const buf = new ArrayBuffer(s.length)
      const view = new Uint8Array(buf)
      for (let i = 0; i < s.length; ++i) {
        view[i] = s.charCodeAt(i) & 0xFF
      }
      return buf
    },
    exportAccessReports (id, format) {
      this.reportedService = {}
      this.allowedUsersReportList = []
      this.bannedUsersReportList = []
      this.allowedGroupsReportList = []
      this.bannedGroupsReportList = []
      this.usersReportList = []
      this.groupsReportList = []
      const vm = this
      let n = 1
      let m = 1
      this.loading = true
      this.axios({
        url: "/api/services/" + id,
        method: "GET"
      }).then((res) => {
        if (res.data.status.code === 200) {
          vm.reportedService = res.data.data
          vm.axios({
            url: "/api/groups",
            method: "GET"
          }).then((res1) => {
            if (res1.data.status.code === 200) {
              vm.groupsReportList = res1.data.data
              for (let i = 0; i < vm.groupsReportList.length; ++i) {
                if (typeof vm.reportedService.accessStrategy.requiredAttributes.ou !== "undefined") {
                  if (vm.reportedService.accessStrategy.requiredAttributes.ou[1].length !== 0) {
                    if (vm.reportedService.accessStrategy.requiredAttributes.ou[1].includes(vm.groupsReportList[i].id)) {
                      vm.groupsReportList[i].recordNumber = n
                      vm.allowedGroupsReportList.push(vm.groupsReportList[i])
                      n += 1
                    } else {
                      vm.groupsReportList[i].recordNumber = m
                      vm.bannedGroupsReportList.push(vm.groupsReportList[i])
                      m += 1
                    }
                  } else {
                    for (let j = 0; j < vm.groupsReportList.length; ++j) {
                      vm.groupsReportList[j].recordNumber = j + 1
                    }
                    vm.allowedGroupsReportList = vm.groupsReportList
                    break
                  }
                } else {
                  for (let j = 0; j < vm.groupsReportList.length; ++j) {
                    vm.groupsReportList[j].recordNumber = j + 1
                  }
                  vm.allowedGroupsReportList = vm.groupsReportList
                  break
                }
              }
              vm.axios({
                url: "/api/users",
                method: "GET"
              }).then((res2) => {
                if (res2.data.status.code === 200) {
                  vm.usersReportList = res2.data.data.userList
                  n = 1
                  m = 1
                  for (let i = 0; i < vm.usersReportList.length; ++i) {
                    if (typeof vm.reportedService.accessStrategy.requiredAttributes.uid !== "undefined") {
                      if (vm.reportedService.accessStrategy.requiredAttributes.uid[1].includes(vm.usersReportList[i]._id)) {
                        vm.usersReportList[i].recordNumber = n
                        vm.allowedUsersReportList.push(vm.usersReportList[i])
                        n += 1
                        continue
                      }
                    }
                    if (typeof vm.reportedService.accessStrategy.rejectedAttributes !== "undefined") {
                      if (typeof vm.reportedService.accessStrategy.rejectedAttributes.uid !== "undefined") {
                        if (vm.reportedService.accessStrategy.rejectedAttributes.uid[1].includes(vm.usersReportList[i]._id)) {
                          vm.usersReportList[i].recordNumber = m
                          vm.bannedUsersReportList.push(vm.usersReportList[i])
                          m += 1
                        }
                      }
                    }
                  }
                  if (format === "pdf") {
                    const doc = new JSPDF()
                    doc.addFileToVFS("IRANSansWeb.ttf", "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AAcAQAOZAVb//wCV/OkGugQFAiYACgAAAAcAQAN8ATQAAf9c/fcAo/8+AAMAABMHJzejo6Sj/pqjpKMAAAL+kP4VAXD/XQADAAcAAAMHJzcFByc3KqKkogI+oqSi/rmio6Olo6SiAAAAAQCgAAABjwWnABIAAAEWEhcWFRQGByc2NzY1NCcKAScBZBEUAwMFDsUOBQMCBxYOBafJ/pqhpoVQ+WMLnJlVVUNEAQcBiocAAQDMAAAClAWkAB0AACEjIi4CNTQ3NTQnLgEnNx4BFxYVFAYHFB4COwEClBR3nFslAggEDgnECQ4EDAECHTVJLRg4aJNct7wlqKJOkTAkM5RNvLYO0sQ8RyYNAP////0AAAICB/4AJgB7AAAABwBrAP0ABf//ACUAAAKUB/4CJgB8AAAABwBrASUABf//AAP9NwIIBacCJgB7AAAABwBsAPgAAP//AGf9NwKUBaQCJgB8AAAABwBsAVwAAP///+IAAALcBwcAJgB7WgAABwBqAUEAAP///+IAAALcBwcCJgB8AAAABwBqAUEAAP///7sAAAJHB2UAJgB7HgAABwBxAUIAAP//AAcAAAKUB2UCJgB8AAAABwBxAY4AAP//AGH99weGA38CJgA1AAAABwB5A94AAP///+z99wJvA5wCJgByAAAABwB5AS8AAP///+z99wLVAuQCJgBzAAAABwB5AVAAAP//AKn99wkgA4oCJgA2AAAABwB5BCIAAP//AGH81weGA38CJgA1AAAABwBAA+QAAP//AKn81wkgA4oCJgA2AAAABwBAA+0AAP///+z81wL5A5wCJgB0AAAABwBAAX8AAP///+z81wNoAuQCJgB1AAAABwBAAX8AAP//AGEAAAeGBHsCJgA1AAAABwA+A/P+l////+wAAAMuBZUCJgB0AAAABwA+Ab7/sf///+wAAANoBPkCJgB1AAAABwA+Ab7/Ff//AKn//QkgBHsCJgA2AAAABwA+BFf+l///AGEAAAeGBYsCJgA1AAAABwA/A/T+kv///+wAAAMbBrcCJgB0AAAABwA/AaL/vv///+wAAANoBhcCJgB1AAAABwA/AaL/Hv//AKn//QkgBYsCJgA2AAAABwA/BFb+kv//AGEAAAeGBc4CJgA1AAAABwBvBDr+fv///+wAAAMGBvICJgB0AAAABwBvAeT/ov///+wAAANoBlUCJgB1AAAABwBvAeT/Bf//AKn//QkgBc4CJgA2AAAABwBvBKX+fv//AE8AAAN9BeQCJgAqAAAABwA+AdYAAP//AIUAAARWBeQCJgAtAAAABwA+AlcAAP///+z81wYfA5QCJgAIAAAABwBAAycAAP///+z82QaXA5QCJgAJAAAABwBAAuMAAv//AF786QYRBBYCJgAHAAAABwA9A5X6a////+z9+QYfA5QCJgAIAAAABwB5AyUAAv///+z99waXA5QCJgAJAAAABwB5AyUAAP//AJX86Qa6BAUCJgAKAAAABwA9A6T6SP//AF786QYRBjQCJgAHAAAABwA9Am4AS////+wAAAYfBZQCJgAIAAAABwA9AnP/q////+wAAAaXBZgCJgAJAAAABwA9AiP/r///AJX86Qa6BhECJgAKAAAABwA9AoEAKP//AGcAAQQIBnACJgALAAAABwA9Ac4Ah///AH8AAAUCBnQCJgBwAAAABwA9Am8Ai///AGcAAQQIB64AJgALAAAABwBvAksAXv//AH8AAAUCB8gCJgBwAAAABwBvAvYAeP///3/9NAJrBMgCJgAMAAAABwA9AYv+3////4b9HgNBBMgCJgANAAAABwA9AYv+3////3/9NAMBBdwAJgAMAAAABwBvAd/+jP///4b9HgNBBdwAJgANAAAABwBvAd/+jP///3/9NAMCBZ8AJgAMAAAABwA/AYn+pv///4b9HgNBBZ8AJgANAAAABwA/AY3+pv//AGv9bQoXBlsCJgAOAAAABwA/B4r/Yv///+z/9AYKBlsCJgAPAAAABwA/A5b/Yv///+z//QdPBf4CJgAQAAAABwA/A+b/Bf//AGz9aQtdBf4CJgARAAAABwA/B8j/Bf//AKf9dwr8BYwCJgASAAAABwA9CVb/o////+z/7AcUBYwCJgATAAAABwA9BYX/o////+z/7AehBYwCJgAUAAAABwA9BXf/o///AIn9dwtIBYwCJgAVAAAABwA9CTL/o///AFsAAAaqBiECJgAWAAAABwA9BSgAN////+wAAAWpBiACJgAXAAAABwA9BEAAN////+wAAAZDBiACJgAYAAAABwA9BEAAN///AFsAAAdfBiECJgAZAAAABwA9BSkAN///AGf9PwWaB1MCJgAaAAAABwA9A5ABav///+wAAAP0BcMCJgAbAAAABwA9AlD/2v///+wAAASMBcICJgAcAAAABwA9Amv/2f//AF/85wVhBh0CJgAdAAAABwA9Az8ANP///+wAAAP9BmACBgAeAAD////sAAAEuAaEAgYAHwAA//8AaQABB10GawImADkAAAAHAD0FzgCC////7AAAA0oGawImADcAAAAHAD0BpACC////7AAAA/0F1QImADgAAAAHAD0B7f/s//8AYP/qCE4F6QImADoAAAAHAD0GWwAA//8AaQABB10HcQAmADkAAAAHAD8FzQB4////7AAAA0oHdwAmADcAAAAHAD8BqgB+////7AAAA/0GuQImADgAAAAHAD8B9v/A//8AYP/qCE4GuQImADoAAAAHAD8GVP/A////7AAAA0oGUQImADcAAAAHAD4BpgBt////7AAAA/0FqQImADgAAAAHAD4B7v/F//8AVv2BBcEFYQImADsAAAAHAD4D9v99//8AVv2BBjYFYQImADwAAAAHAD4ED/99////7AAAAm8FigImAHIAAAAHAD0BK/+h////7AAAAtUE4QImAHMAAAAHAD0BKv74//8Aa/1RBe8DKgImAFkAAAAHAD0DNP1B//8AOv1SBsMDKgImAFoAAAAHAD0DAP1B//8ATwAAA30EIQIGACoAAP//AE8AAAN9BpsAJgAqAAAABwBrAaP+ov//AIUAAARWBpECJgAtAAAABwBrAiv+mP//AE39QgPIBdMAJgAuAAAABwBrAcz92v//AE79QgQnBdMCJgAvAAAABwBrAcz92v///+wAAAJvA5wABgByAAD////sAAAC1QLkAgYAcwAA////7P4VAuIDnAAmAHQAAAAHAHoBcAAA////7P4VA2gC5AImAHUAAAAHAHoBfgAA//8AX/uFBtADSwImADAAAAAHAHoDLf1w//8AYvueBscCjAImADEAAAAHAHoDHv2J//8AX/1BBtADSwIGADAAAP///+z+FQLiA5wCJgB0AAAABwB6AXIAAP///+z+FQNoAuQCJgB1AAAABwB6AXIAAP//AGL9dAbHAowCBgAxAAD//wBS/mEFEgNZAiYAUQAAAAcAawFv+2D//wBS/mEFWANBAiYAUgAAAAcAawGy+0j//wBf/UEG0AT/ACYAMAAAAAcAawJt/Qb////sAAAC4gYlACYAdAAAAAcAawGn/iz////sAAADaAV6AiYAdQAA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AGQAZQBtAGEAcgBrACAAbwBmACAAdwB3AHcALgBmAG8AbgB0AGkAcgBhAG4ALgBjAG8AbQAgACgATQBvAHMAbABlAG0AIABFAGIAcgBhAGgAaQBtAGkAKQAuAFQAbwAgAHUAcwBlACAAdABoAGkAcwAgAGYAbwBuAHQALAAgAGkAdAAgAGkAcwAgAG4AZQBjAGUAcwBzAGEAcgB5ACAAdABvACAAbwBiAHQAYQBpAG4AIAB0AGgAZQAgAGwAaQBjAGUAbgBzAGUAIABmAHIAbwBtACAAdwB3AHcALgBmAG8AbgB0AGkAcgBhAG4ALgBjAG8AbQBoAHQAdABwADoALwAvAHcAdwB3AC4AbQBvAHMAbABlAG0AZQBiAHIAYQBoAGkAbQBpAC4AYwBvAG0AQwBvAHAAeQByAGkAZwBoAHQAIAAoAGMAKQAgADIAMAAxADcAIABiAHkAIABoAHQAdABwADoALwAvAHcAdwB3AC4AZgBvAG4AdABpAHIAYQBuAC4AYwBvAG0AIABBAGwAbAAgAFIAaQBnAGgAdABzACAAUgBlAHMAZQByAHYAZQBkAC4AAAACAAAAAAAA/agAZAAAAAAAAAAAAAAAAAAAAAAAAAAAAewAAAECAAIAAwEDAQQBBQEGAQcBCAEJAQoBCwEMAQ0BDgEPARABEQESARMBFAEVARYBFwEYARkBGgEbARwBHQEeAR8BIAEhASIBIwEkASUBJgEnASgBKQEqASsBLAEtAS4BLwEwATEBMgEzATQBNQE2ATcBOAE5AToBOwE8AT0BPgE/AUABQQFCAUMBRAFFAUYBRwFIAUkBSgFLAUwBTQFOAU8BUAFRAVIBUwFUAVUBVgFXAVgBWQFaAVsBXAFdAV4BXwFgAWEBYgFjAWQBZQFmAWcBaAFpAWoBawFsAW0BbgFvAXABcQFyAXMBdAF1AXYBdwF4AXkBegF7AXwBfQF+AX8BgAGBAYIBgwGEAYUBhgGHAYgBiQGKAYsBjAGNAY4BjwGQAZEBkgGTAZQBlQGWAZcBmAGZAZoBmwGcAZ0BngGfAaABoQGiAaMBpAGlAaYBpwGoAakBqgGrAawBrQGuAa8BsAGxAbIBswG0AbUBtgG3AbgBuQG6AbsBvAG9Ab4BvwHAAcEBwgHDAcQBxQHGAccByAHJAcoBywHMAc0BzgHPAdAB0QHSAdMB1AHVAdYB1wHYAdkB2gHbAdwB3QHeAd8B4AHhAeIB4wHkAeUB5gHnAegB6QHqAesB7AHtAe4B7wHwAfEB8gHzAfQB9QH2AfcB+AH5AfoB+wH8Af0B/gH/AgACAQICAgMCBAIFAgYCBwIIAgkCCgILAgwCDQIOAg8CEAIRAhICEwIUAhUCFgIXAhgCGQIaAhsCHAIdAh4CHwIgAiECIgIjAAQACwAMABEAHQAFAAYABwAIAAkACgANAA4ADwAQABIAEwAUABUAFgAXABgAGQAaABsAHAAeAB8AIAAhACIAIwAkACUAJgAnACgAKQAqACsALAAtAC4ALwAwADEAMgAzADQANQA2ADcAOAA5ADoAOwA8AD0APgA/AEAAQQBCAEMARABFAEYARwBIAEkASgBLAEwATQBOAE8AUABRAFIAUwBUAFUAVgBXAFgAWQBaAFsAXABdAF4AXwBgAGEAiwCpAIoAqgDwALgCJAIlAiYCJwIoAikCKgIrAiwCLQIuAi8CMACyALMAtgC3AMQCMQC0ALUAxQC+AL8AhwIyAKsCMwI0ALwCNQI2AjcCOAI5AjoAowCEAIUAvQCWAOgAhgCOAJ0ApAI7ANoAgwCTAPIA8wCNAJcAiADDAN4A8QCeAPUA9AD2AKIAkACRAO0AiQCgAOoA2ADhAjwCPQDbANwA3QDgANkA3wI+Aj8CQACCAMICQQDGAkIAmACoAJoAmQDvAKUAkgCcAKcAjwCUAJUETlVMTAd1bmkwNjIxB3VuaTA2MjcMdW5pMDYyNy5maW5hB3VuaTA2MkQMdW5pMDYyRC5pbml0DHVuaTA2MkQubWVkaQx1bmkwNjJELmZpbmEHdW5pMDYyRgd1bmkwNjMxDHVuaTA2MzEuZmluYQd1bmkwNjMzDHVuaTA2MzMuaW5pdAx1bmkwNjMzLm1lZGkMdW5pMDYzMy5maW5hB3VuaTA2MzUMdW5pMDYzNS5pbml0DHVuaTA2MzUubWVkaQx1bmkwNjM1LmZpbmEHdW5pMDYzNwx1bmkwNjM3LmluaXQMdW5pMDYzNy5tZWRpDHVuaTA2MzcuZmluYQd1bmkwNjM5DHVuaTA2MzkuaW5pdAx1bmkwNjM5Lm1lZGkMdW5pMDYzOS5maW5hDHVuaTA2NDMuaW5pdAx1bmkwNjQzLm1lZGkHdW5pMDY0Mwx1bmkwNjQzLmZpbmEHdW5pMDY0NAx1bmkwNjQ0LmluaXQMdW5pMDY0NC5tZWRpDHVuaTA2NDQuZmluYQd1bmkwNjQ1DHVuaTA2NDUuaW5pdAx1bmkwNjQ1Lm1lZGkMdW5pMDY0NS5maW5hB3VuaTA2NDcMdW5pMDY0Ny5pbml0DHVuaTA2NDcubWVkaQx1bmkwNjQ3LmZpbmEHdW5pMDY0OAx1bmkwNjQ4LmZpbmEHdW5pMDY0OQx1bmkwNjQ5LmZpbmEQdW5pMDY0NDA2MjcuaXNvbBB1bmkwNjQ0MDYyNy5maW5hB3VuaTA2NDAHdW5pMDY2RQx1bmkwNjZFLmZpbmEMdW5pMDY2Ri5pbml0DHVuaTA2NkYubWVkaRBmZWhfZG90bGVzcy5pc29sDHVuaTA2QTEuZmluYQd1bmkwNjZGDHVuaTA2NkYuZmluYQxvbmVkb3QuYWJvdmUNdHdvZG90cy5hYm92ZQ90aHJlZWRvdHMuYWJvdmUPdGhyZWVkb3RzLmJlbG93B3VuaTA2NjAHdW5pMDY2MQd1bmkwNjYyB3VuaTA2NjMHdW5pMDY2NAd1bmkwNjY1B3VuaTA2NjYHdW5pMDY2Nwd1bmkwNjY4B3VuaTA2NjkHdW5pMDZGNAd1bmkwNkY1B3VuaTA2RjYMdW5pMDZDMS5pbml0DHVuaTA2QzEubWVkaQd1bmkwNkJFB3VuaTA2RDIMdW5pMDZEMi5maW5hB3VuaTA2QTkMdW5pMDZBOS5maW5hB3VuaTA2QUYMdW5pMDZBRi5pbml0DHVuaTA2QUYubWVkaQx1bmkwNkFGLmZpbmEHdW5pMDZCQQx1bmkwNkJBLmZpbmEHdW5pMDY2QQd1bmkwNjFGB3VuaTA2MEMHdW5pMDYxQgd1bmkwNjZCB3VuaTA2NkMHdW5pMDY2RAd1bmkwNjRCB3VuaTA2NEQHdW5pMDY0RQd1bmkwNjRGB3VuaTA2NEMHdW5pMDY1MAd1bmkwNjUxB3VuaTA2NTIHdW5pMDY1Mwd1bmkwNjU0B3VuaTA2NTUHdW5pMDY3MAd1bmkwNjU2B3VuaTA2MTUMdW5pMDYyRi5maW5hBXdhc2xhDHVuaTA2NkUuaW5pdAx1bmkwNjZFLm1lZGkUYmVoX2RvdGxlc3NfYWx0LmluaXQUYmVoX2RvdGxlc3NfYWx0Lm1lZGkMdW5pMDZDMS5maW5hB3VuaTA2ODYMdW5pMDY4Ni5maW5hDG9uZWRvdC5iZWxvdw10d29kb3RzLmJlbG93DWFsZWZfYWx0Lmlzb2wNYWxlZl9hbHQuZmluYQd1bmkwNjIzDHVuaTA2MjMuZmluYQd1bmkwNjI1DHVuaTA2MjUuZmluYQd1bmkwNjIyDHVuaTA2MjIuZmluYQd1bmkwNjcxDHVuaTA2NzEuZmluYQd1bmkwNjI4DHVuaTA2MjguaW5pdAx1bmkwNjI4Lm1lZGkMdW5pMDYyOC5maW5hB3VuaTA2N0UMdW5pMDY3RS5maW5hDHVuaTA2N0UuaW5pdAx1bmkwNjdFLm1lZGkHdW5pMDYyQQx1bmkwNjJBLmluaXQMdW5pMDYyQS5tZWRpDHVuaTA2MkEuZmluYQd1bmkwNjJCDHVuaTA2MkIuaW5pdAx1bmkwNjJCLm1lZGkMdW5pMDYyQi5maW5hB3VuaTA2NzkMdW5pMDY3OS5pbml0DHVuaTA2NzkubWVkaQx1bmkwNjc5LmZpbmEHdW5pMDYyOQx1bmkwNjI5LmZpbmEMdW5pMDY4Ni5pbml0DHVuaTA2ODYubWVkaQd1bmkwNjJDDHVuaTA2MkMuaW5pdAx1bmkwNjJDLm1lZGkMdW5pMDYyQy5maW5hB3VuaTA2MkUMdW5pMDYyRS5pbml0DHVuaTA2MkUubWVkaQx1bmkwNjJFLmZpbmEHdW5pMDYzMAx1bmkwNjMwLmZpbmEHdW5pMDY4OAx1bmkwNjg4LmZpbmEHdW5pMDYzMgx1bmkwNjMyLmZpbmEHdW5pMDY5MQx1bmkwNjkxLmZpbmEHdW5pMDY5OAx1bmkwNjk4LmZpbmEHdW5pMDYzNAx1bmkwNjM0LmluaXQMdW5pMDYzNC5tZWRpDHVuaTA2MzQuZmluYQd1bmkwNjM2DHVuaTA2MzYuaW5pdAx1bmkwNjM2Lm1lZGkMdW5pMDYzNi5maW5hB3VuaTA2MzgMdW5pMDYzOC5pbml0DHVuaTA2MzgubWVkaQx1bmkwNjM4LmZpbmEHdW5pMDYzQQx1bmkwNjNBLmluaXQMdW5pMDYzQS5tZWRpDHVuaTA2M0EuZmluYQx1bmkwNkE5LmluaXQMdW5pMDZBOS5tZWRpB3VuaTA2NDEMdW5pMDY0MS5pbml0DHVuaTA2NDEubWVkaQx1bmkwNjQxLmZpbmEIdmVoLmlzb2wIdmVoLmluaXQIdmVoLm1lZGkIdmVoLmZpbmEMdW5pMDY0Mi5pbml0DHVuaTA2NDIubWVkaQd1bmkwNjQyDHVuaTA2NDIuZmluYQx1bmkwNjQ2LmluaXQMdW5pMDY0Ni5tZWRpB3VuaTA2NDYMdW5pMDY0Ni5maW5hB3VuaTA2RDUHdW5pMDZDMAx1bmkwNkMwLmZpbmEHdW5pMDYyNAx1bmkwNjI0LmZpbmEMdW5pMDY0OS5pbml0DHVuaTA2NDkubWVkaQx1bmkwNjRBLmluaXQMdW5pMDY0QS5tZWRpB3VuaTA2NEEMdW5pMDY0QS5maW5hB3VuaTA2Q0MMdW5pMDZDQy5pbml0DHVuaTA2Q0MubWVkaQx1bmkwNkNDLmZpbmEHdW5pMDZEMwx1bmkwNkQzLmZpbmEHdW5pMDYyNgx1bmkwNjI2LmluaXQMdW5pMDYyNi5tZWRpDHVuaTA2MjYuZmluYRB1bmkwNjQ0MDYyMy5pc29sEHVuaTA2NDQwNjIzLmZpbmEQdW5pMDY0NDA2MjUuaXNvbBB1bmkwNjQ0MDYyNS5maW5hEHVuaTA2NDQwNjIyLmlzb2wQdW5pMDY0NDA2MjIuZmluYRB1bmkwNjQ0MDY3MS5pc29sEHVuaTA2NDQwNjcxLmZpbmELaGVoX2FlLmZpbmEHdW5pMDZDMgx1bmkwNkMyLmZpbmEHdW5pMDZDMQd1bmkwNkMzDHVuaTA2QzMuZmluYQx1bmkwNkJFLmZpbmEMdW5pMDZCRS5pbml0DHVuaTA2QkUubWVkaQd1bmkwNkYwB3VuaTA2RjEHdW5pMDZGMgd1bmkwNkYzB3VuaTA2RjkHdW5pMDZGNwd1bmkwNkY4DHVuaTA2RjQudXJkdQx1bmkwNkY3LnVyZHULZmF0aGF0YW5fMDELZGFtbWF0YW5fMDELa2FzcmF0YW5fMDEIZmF0aGFfMDEIZGFtbWFfMDEIa2FzcmFfMDEJc2hhZGRhXzAxCHN1a3VuXzAxDEdodW5uYV9hYm92ZQt1bmkwNjUxMDY0Qwt1bmkwNjUxMDY0RAt1bmkwNjUxMDY0Qgt1bmkwNjUxMDY0RQt1bmkwNjUxMDY0Rgt1bmkwNjUxMDY1MAd1bmlGQzYzC3VuaTA2NTQwNjRCC3VuaTA2NTQwNjRFC3VuaTA2NTQwNjRDC3Vu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")
                    doc.addFont("IRANSansWeb.ttf", "IRANSansWeb", "normal")
                    doc.setFont("IRANSansWeb")
                    const width = doc.internal.pageSize.getWidth()
                    doc.text(this.$t("serviceAccessReport"), width - 10, 10, "right")
                    autoTable(doc, {
                      startY: 20,
                      body: [[this.reportedService.id, this.$t("id")],
                        [this.reportedService.name, this.$t("name")],
                        [this.reportedService.extraInfo.url, this.$t("url")],
                        [this.reportedService.description, this.$t("persianName")]],
                      styles: {
                        font: "IRANSansWeb",
                        halign: "right"
                      }
                    })

                    let tempArray = []
                    let tempElement = []
                    for (let i = 0; i < this.allowedUsersReportList.length; ++i) {
                      tempElement.push(this.allowedUsersReportList[i].displayName.toString())
                      tempElement.push(this.allowedUsersReportList[i]._id.toString())
                      tempElement.push(this.allowedUsersReportList[i].recordNumber.toString())
                      tempArray.push(tempElement)
                      tempElement = []
                    }
                    let finalY = doc.lastAutoTable.finalY
                    doc.text(this.$t("authorizedUsers"), width - 10, finalY + 10, "right")
                    autoTable(doc, {
                      startY: finalY + 20,
                      head: [[this.$t("persianName"), this.$t("id"), "#"]],
                      body: tempArray,
                      styles: {
                        font: "IRANSansWeb",
                        halign: "right"
                      },
                      headStyles: {
                        fillColor: "28a745",
                        textColor: "ffffff"
                      }
                    })

                    tempArray = []
                    tempElement = []
                    for (let i = 0; i < this.bannedUsersReportList.length; ++i) {
                      tempElement.push(this.bannedUsersReportList[i].displayName.toString())
                      tempElement.push(this.bannedUsersReportList[i]._id.toString())
                      tempElement.push(this.bannedUsersReportList[i].recordNumber.toString())
                      tempArray.push(tempElement)
                      tempElement = []
                    }
                    finalY = doc.lastAutoTable.finalY
                    doc.text(this.$t("bannedUsers"), width - 10, finalY + 10, "right")
                    autoTable(doc, {
                      startY: finalY + 20,
                      head: [[this.$t("persianName"), this.$t("id"), "#"]],
                      body: tempArray,
                      styles: {
                        font: "IRANSansWeb",
                        halign: "right"
                      },
                      headStyles: {
                        fillColor: "dc3545",
                        textColor: "ffffff"
                      }
                    })

                    tempArray = []
                    tempElement = []
                    for (let i = 0; i < this.allowedGroupsReportList.length; ++i) {
                      for (let j = 0; j < this.usersReportList.length; ++j) {
                        if (typeof this.usersReportList[j].memberOf !== "undefined" && this.usersReportList[j].memberOf !== null) {
                          if (this.usersReportList[j].memberOf.includes(this.allowedGroupsReportList[i].id)) {
                            tempElement.push(this.usersReportList[j].displayName.toString())
                            tempElement.push(this.usersReportList[j]._id.toString())
                            tempElement.push(this.allowedGroupsReportList[i].name.toString())
                            tempElement.push(this.allowedGroupsReportList[i].id.toString())
                            tempElement.push(this.allowedGroupsReportList[i].recordNumber.toString())
                            tempArray.push(tempElement)
                            tempElement = []
                          }
                        }
                      }
                    }
                    finalY = doc.lastAutoTable.finalY
                    doc.text(this.$t("authorizedGroups"), width - 10, finalY + 10, "right")
                    autoTable(doc, {
                      startY: finalY + 20,
                      head: [[this.$t("userFaNameInGroup"), this.$t("idInGroup"), this.$t("persianName"), this.$t("name"), "#"]],
                      body: tempArray,
                      styles: {
                        font: "IRANSansWeb",
                        halign: "right"
                      },
                      headStyles: {
                        fillColor: "dc3545",
                        textColor: "ffffff"
                      }
                    })

                    tempArray = []
                    tempElement = []
                    for (let i = 0; i < this.bannedGroupsReportList.length; ++i) {
                      for (let j = 0; j < this.usersReportList.length; ++j) {
                        if (typeof this.usersReportList[j].memberOf !== "undefined" && this.usersReportList[j].memberOf !== null) {
                          if (this.usersReportList[j].memberOf.includes(this.bannedGroupsReportList[i].id)) {
                            tempElement.push(this.usersReportList[j].displayName.toString())
                            tempElement.push(this.usersReportList[j]._id.toString())
                            tempElement.push(this.bannedGroupsReportList[i].name.toString())
                            tempElement.push(this.bannedGroupsReportList[i].id.toString())
                            tempElement.push(this.bannedGroupsReportList[i].recordNumber.toString())
                            tempArray.push(tempElement)
                            tempElement = []
                          }
                        }
                      }
                    }
                    finalY = doc.lastAutoTable.finalY
                    doc.text(this.$t("bannedGroups"), width - 10, finalY + 10, "right")
                    autoTable(doc, {
                      startY: finalY + 20,
                      head: [[this.$t("userFaNameInGroup"), this.$t("idInGroup"), this.$t("persianName"), this.$t("name"), "#"]],
                      body: tempArray,
                      styles: {
                        font: "IRANSansWeb",
                        halign: "right"
                      },
                      headStyles: {
                        fillColor: "dc3545",
                        textColor: "ffffff"
                      }
                    })
                    doc.save("Parsso-ServiceReport-" + this.reportedService.name + ".pdf")
                  } else if (format === "xlsx") {
                    const fileName = "Parsso-ServiceReport-" + this.reportedService.name
                    const fileData = [[this.$t("id"), this.reportedService.id, "", this.$t("persianName"), this.reportedService.displayName, "", this.$t("mobile"), this.reportedService.mobile, "", this.$t("mail"), this.reportedService.mail],
                      [""],
                      ["", this.$t("authorizedUsers"), "", "", "", this.$t("bannedUsers"), "", "", "", "", this.$t("authorizedGroups"), "", "", "", "", "", this.$t("bannedGroups")],
                      ["#", this.$t("id"), this.$t("persianName"), "", "#", this.$t("id"), this.$t("persianName"), "", "#", this.$t("id"), this.$t("persianName"), this.$t("idInGroup"),
                        this.$t("userFaNameInGroup"), "", "#", this.$t("id"), this.$t("persianName"), this.$t("idInGroup"), this.$t("userFaNameInGroup")]]

                    const tempArray11 = []
                    let tempElement11 = {}
                    const tempArray = []
                    let tempElement = {}
                    for (let i = 0; i < this.allowedGroupsReportList.length; ++i) {
                      for (let j = 0; j < this.usersReportList.length; ++j) {
                        if (typeof this.usersReportList[j].memberOf !== "undefined" && this.usersReportList[j].memberOf !== null) {
                          if (this.usersReportList[j].memberOf.includes(this.allowedGroupsReportList[i].id)) {
                            tempElement.displayName = this.usersReportList[j].displayName.toString()
                            tempElement._id = this.usersReportList[j]._id.toString()
                            tempElement.name = this.allowedGroupsReportList[i].name.toString()
                            tempElement.id = this.allowedGroupsReportList[i].id.toString()
                            tempElement.recordNumber = this.allowedGroupsReportList[i].recordNumber.toString()
                            tempArray.push(tempElement)
                            tempElement = {}
                          }
                        }
                      }
                    }
                    for (let i = 0; i < this.bannedGroupsReportList.length; ++i) {
                      for (let j = 0; j < this.usersReportList.length; ++j) {
                        if (typeof this.usersReportList[j].memberOf !== "undefined" && this.usersReportList[j].memberOf !== null) {
                          if (this.usersReportList[j].memberOf.includes(this.bannedGroupsReportList[i].id)) {
                            tempElement11.displayName = this.usersReportList[j].displayName.toString()
                            tempElement11._id = this.usersReportList[j]._id.toString()
                            tempElement11.name = this.bannedGroupsReportList[i].name.toString()
                            tempElement11.id = this.bannedGroupsReportList[i].id.toString()
                            tempElement11.recordNumber = this.bannedGroupsReportList[i].recordNumber.toString()
                            tempArray11.push(tempElement11)
                            tempElement11 = {}
                          }
                        }
                      }
                    }
                    const maxLength = Math.max(this.allowedUsersReportList.length, this.bannedUsersReportList.length, tempArray.length, tempArray11.length)
                    let tempArray1 = new Array(maxLength - this.allowedUsersReportList.length).fill({ recordNumber: "", _id: "", displayName: "" })
                    let tempArray2 = new Array(maxLength - this.bannedUsersReportList.length).fill({ recordNumber: "", _id: "", displayName: "" })
                    let tempArray3 = new Array(maxLength - tempArray.length).fill({ recordNumber: "", id: "", name: "", _id: "", displayName: "" })
                    let tempArray4 = new Array(maxLength - tempArray11.length).fill({ recordNumber: "", id: "", name: "", _id: "", displayName: "" })
                    tempArray1 = this.allowedUsersReportList.concat(tempArray1)
                    tempArray2 = this.bannedUsersReportList.concat(tempArray2)
                    tempArray3 = tempArray.concat(tempArray3)
                    tempArray4 = tempArray11.concat(tempArray4)
                    for (let i = 0; i < maxLength; ++i) {
                      fileData.push([tempArray1[i].recordNumber, tempArray1[i].userId, tempArray1[i].displayName, "",
                        tempArray2[i].recordNumber, tempArray2[i].userId, tempArray2[i].displayName, "",
                        tempArray3[i].recordNumber, tempArray3[i].id, tempArray3[i].name, tempArray3[i].userId, tempArray3[i].displayName, "",
                        tempArray4[i].recordNumber, tempArray4[i].id, tempArray4[i].name, tempArray4[i].userId, tempArray4[i].displayName])
                    }
                    const wb = this.XLSX.utils.book_new()
                    wb.Props = {
                      Title: fileName,
                      Subject: fileName,
                      Author: "Parsso"
                    }
                    wb.Views = {
                      RTL: true
                    }
                    wb.SheetNames.push(fileName)
                    const ws = this.XLSX.utils.aoa_to_sheet(fileData)
                    wb.Sheets[fileName] = ws
                    const wbout = this.XLSX.write(wb, { bookType: "xlsx", type: "binary" })
                    const fileBlob = new Blob([this.s2ab(wbout)], { type: "application/octet-stream" })
                    if (window.navigator.msSaveOrOpenBlob) {
                      window.navigator.msSaveOrOpenBlob(fileBlob, fileName + ".xlsx")
                    } else {
                      const a = document.createElement("a")
                      const url = URL.createObjectURL(fileBlob)
                      a.href = url
                      a.download = fileName + ".xlsx"
                      document.body.appendChild(a)
                      a.click()
                      setTimeout(function () {
                        document.body.removeChild(a)
                        window.URL.revokeObjectURL(url)
                      }, 0)
                    }
                  }
                  vm.loading = false
                } else {
                  vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                  vm.loading = false
                }
              }).catch(() => {
                vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                vm.loading = false
              })
            } else {
              vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
              vm.loading = false
            }
          }).catch(() => {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loading = false
          })
        } else {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loading = false
        }
      }).catch(() => {
        vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
        vm.loading = false
      })
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
