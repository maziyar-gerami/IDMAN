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
                  <h4>{{ $t("serviceInformation") }}</h4>
                  <div class="formgrid grid">
                    <div class="field col-2">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.accessStrategy.enabled" class="flex">{{ $t("enableService") }}</label>
                        <InputSwitch id="createServiceBuffer.accessStrategy.enabled" v-model="createServiceBuffer.accessStrategy.enabled" />
                      </div>
                    </div>
                    <div class="field col-2">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.accessStrategy.ssoEnabled" class="flex">{{ $t("allowSSO") }}</label>
                        <InputSwitch id="createServiceBuffer.accessStrategy.ssoEnabled" v-model="createServiceBuffer.accessStrategy.ssoEnabled" />
                      </div>
                    </div>
                    <div class="field col-4">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.serviceType">{{ $t("serviceType") }}</label>
                        <Dropdown id="createServiceBuffer.serviceType" :options="['CAS','SAML','Oauth2']" v-model="createServiceBuffer.serviceType" />
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
                        <InputText id="createServiceBuffer.serviceId" type="text" :class="createServiceErrors.serviceId" v-model="createServiceBuffer.serviceId" />
                        <small>{{ $t("serviceURLText1") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.extraInfo.url">{{ $t("serviceURL") }}<span style="color: red;"> * </span></label>
                        <InputText id="createServiceBuffer.extraInfo.url" type="text" :class="createServiceErrors.extraInfo.url" v-model="createServiceBuffer.extraInfo.url" />
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
                        <InputText id="createServiceBuffer.metadataLocation" type="text" :class="createServiceErrors.metadataLocation" v-model="createServiceBuffer.metadataLocation" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                      <div v-else-if="createServiceBuffer.metadataLocationOption === 'file'" class="field p-fluid">
                        <FileUpload id="createServiceBuffer.metadataLocation" mode="basic" name="file" :chooseLabel="$t('selectFile')"
                        @select="fileUploadHelper($event, 'create', 'metadata')" :class="createServiceErrors.metadataLocation" />
                        <input id="createMetadataFile" type="file" class="hidden" name="file">
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
                          <Button @click="generateSecret()">{{ $t("generateSecret") }}</Button>
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
                        @select="fileUploadHelper($event, 'create', 'logo')" />
                        <input id="createLogoFile" type="file" class="hidden" name="file">
                      </div>
                    </div>
                  </div>
                  <div class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.informationUrl">{{ $t("informationURL") }}</label>
                        <InputText id="createServiceBuffer.informationUrl" type="text" v-model="createServiceBuffer.informationUrl" />
                        <small>{{ $t("serviceURLText2") }}</small>
                      </div>
                    </div>
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.privacyUrl">{{ $t("privacyURL") }}</label>
                        <InputText id="createServiceBuffer.privacyUrl" type="text" v-model="createServiceBuffer.privacyUrl" />
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
                        <InputText id="createServiceBuffer.logoutUrl" type="text" v-model="createServiceBuffer.logoutUrl" />
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
                        <AppListBox :label="$t('users')" :list="users" :searchParameters="['_id', 'displayName']" :loader="createServiceUsersLoader"
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
                        <AppDayTimeRange startDateId="createServiceBuffer.dailyAccess.start" endDateId="createServiceBuffer.dailyAccess.end" />
                      </div>
                    </div>
                  </div>
                  <div v-if="createServiceBuffer.dailyAccessType.id === 'URL'" class="formgrid grid">
                    <div class="field col">
                      <div class="field p-fluid">
                        <label for="createServiceBuffer.accessStrategy.endpointUrl">{{ $t("url") }}</label>
                        <InputText id="createServiceBuffer.accessStrategy.endpointUrl" type="text" v-model="createServiceBuffer.accessStrategy.endpointUrl" />
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
        id: "",
        name: ""
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
      editServiceBuffer: {},
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
        for (let i = 0; i < 7; ++i) {
          if (document.getElementById("select" + i).checked) {
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
            this.createServiceBuffer.extraInfo.dailyAccess.push(tempDailyAccessDay)
          }
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

          const dateEndTemp = document.getElementsByName("dateEnd")[0].value.split("  ")
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

          this.accessStrategy.startingDateTime = dateStartFinal + "T" + timeStart
          this.accessStrategy.endingDateTime = dateEndFinal + "T" + timeEnd
        }
        for (const i in this.createServiceBuffer.attributeList) {
          if (this.createServiceBuffer.attributeList[i].name !== "" && this.createServiceBuffer.attributeList[i].value !== "") {
            this.createServiceBuffer.accessStrategy.requiredAttributes[this.createServiceBuffer.attributeList[i].name] =
            ["java.util.HashSet", this.createServiceBuffer.attributeList[i].value.split(",")]
          }
        }

        let serviceType = ""
        if (this.createServiceBuffer.serviceType === "CAS") {
          serviceType = "cas"
          serviceData = JSON.stringify({
            name: vm.createServiceBuffer.name,
            serviceId: vm.createServiceBuffer.serviceId,
            extraInfo: vm.createServiceBuffer.extraInfo,
            multifactorPolicy: vm.createServiceBuffer.multifactorPolicy,
            description: vm.createServiceBuffer.description,
            logo: vm.createServiceBuffer.logo,
            informationUrl: vm.createServiceBuffer.informationUrl,
            privacyUrl: vm.createServiceBuffer.privacyUrl,
            logoutType: vm.createServiceBuffer.logoutType,
            logoutUrl: vm.createServiceBuffer.logoutUrl,
            accessStrategy: vm.createServiceBuffer.accessStrategy,
            contacts: vm.createServiceBuffer.contacts
          }).replace(/\\\\/g, "\\")
        } else if (this.createServiceBuffer.serviceType === "SAML") {
          serviceType = "saml"
          serviceData = JSON.stringify({
            name: vm.createServiceBuffer.name,
            serviceId: vm.createServiceBuffer.serviceId,
            extraInfo: vm.createServiceBuffer.extraInfo,
            metadataLocation: vm.createServiceBuffer.metadataLocation,
            multifactorPolicy: vm.createServiceBuffer.multifactorPolicy,
            description: vm.createServiceBuffer.description,
            logo: vm.createServiceBuffer.logo,
            informationUrl: vm.createServiceBuffer.informationUrl,
            privacyUrl: vm.createServiceBuffer.privacyUrl,
            logoutType: vm.createServiceBuffer.logoutType,
            logoutUrl: vm.createServiceBuffer.logoutUrl,
            accessStrategy: vm.createServiceBuffer.accessStrategy,
            contacts: vm.createServiceBuffer.contacts
          }).replace(/\\\\/g, "\\")
        } else if (this.createServiceBuffer.serviceType === "Oauth2") {
          serviceType = "oauth"
          serviceData = JSON.stringify({
            name: vm.createServiceBuffer.name,
            serviceId: vm.createServiceBuffer.serviceId,
            clientId: vm.createServiceBuffer.clientId,
            clientSecret: vm.createServiceBuffer.clientSecret,
            supportedGrantTypes: vm.createServiceBuffer.supportedGrantTypes,
            supportedResponseTypes: vm.createServiceBuffer.supportedResponseTypes,
            extraInfo: vm.createServiceBuffer.extraInfo,
            metadataLocation: vm.createServiceBuffer.metadataLocation,
            multifactorPolicy: vm.createServiceBuffer.multifactorPolicy,
            description: vm.createServiceBuffer.description,
            logo: vm.createServiceBuffer.logo,
            informationUrl: vm.createServiceBuffer.informationUrl,
            privacyUrl: vm.createServiceBuffer.privacyUrl,
            logoutType: vm.createServiceBuffer.logoutType,
            logoutUrl: vm.createServiceBuffer.logoutUrl,
            accessStrategy: vm.createServiceBuffer.accessStrategy,
            contacts: vm.createServiceBuffer.contacts
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
          if (res.data.status.code === 200) {
            vm.createServiceLoader = false
            vm.resetState("createService")
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
            vm.users = res.data.data
            vm.createServiceUsersLoader = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createServiceUsersLoader = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.createServiceUsersLoader = false
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

      if (this.createServiceBuffer.serviceType === "SAML") {
        if (this.createServiceBuffer.metadataLocationOption === "address") {
          if (this.createServiceBuffer.metadataLocation === "") {
            this.createServiceErrors.metadataLocation = "p-invalid"
            errorCount += 1
          } else {
            this.createServiceErrors.metadataLocation = ""
          }
        } else if (this.createServiceBuffer.metadataLocationOption === "file") {
          const metadataFileBodyFormData = new FormData()
          metadataFileBodyFormData.append("file", this.createServiceBuffer.metadataFile)
          const vm = this
          this.createServiceLoader = true
          this.axios({
            url: "/api/services/metadata",
            method: "POST",
            headers: { "Content-Type": "multipart/form-data" },
            data: metadataFileBodyFormData
          }).then((res) => {
            if (res.data === "") {
              vm.createServiceErrors.metadataLocation = "p-invalid"
              errorCount += 1
            } else {
              vm.createServiceErrors.metadataLocation = ""
            }
            vm.createServiceLoader = false
          }).catch(() => {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
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
      }

      const i = setInterval(function () {
        if (this.createServiceLoader === false) {
          clearInterval(i)
        }
      }, 500)

      if (this.createServiceBuffer.logoFile !== null) {
        const logoFileBodyFormData = new FormData()
        logoFileBodyFormData.append("file", this.createServiceBuffer.logoFile)
        const vm = this
        this.createServiceLoader = true
        this.axios({
          url: "/api/services/icon",
          method: "POST",
          headers: { "Content-Type": "multipart/form-data" },
          data: logoFileBodyFormData
        }).then((res) => {
          if (res.data !== "") {
            vm.createServiceBuffer.logo = res.data
          }
          vm.createServiceLoader = false
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.createServiceLoader = false
        })
      }

      const j = setInterval(function () {
        if (this.createServiceLoader === false) {
          clearInterval(j)
        }
      }, 500)

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
      /* this.servicesRequestMaster("getService")
      this.servicesRequestMaster("getUsersEditService") */
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
        this.tabActiveIndexList = 0
        this.createServiceFlag = false
        /* this.createServiceBuffer = {
          id: "",
          name: "",
          description: "",
          usersCount: null,
          usersList: [[], []]
        } */
      } else if (command === "editService") {
        this.tabActiveIndexList = 0
        this.editServiceFlag = false
        /* this.editServiceBuffer = {
          id: "",
          name: "",
          description: "",
          usersCount: null,
          usersList: [[], []]
        } */
      }
    },
    generateSecret () {
      this.createServiceBuffer.clientSecret = Math.random().toString(36).substring(2, 15) + Math.random().toString(36).substring(2, 15)
    },
    createServiceAllUsersToggle (user, type, command) {
      if (type === "list") {
        if (command === "add") {
          this.createServiceBuffer.accessStrategy.requiredAttributes.uid[1].push(user)
        } else if (command === "remove") {
          this.createServiceBuffer.accessStrategy.rejectedAttributes.uid[1].push(user)
        }
        this.users = this.users.filter((item) => item._id !== user._id)
      } else if (type === "authorize") {
        this.users.push(user)
        this.createServiceBuffer.accessStrategy.requiredAttributes.uid[1] = this.createServiceBuffer.accessStrategy.requiredAttributes.uid[1].filter((item) => item._id !== user._id)
      } else if (type === "ban") {
        this.users.push(user)
        this.createServiceBuffer.accessStrategy.rejectedAttributes.uid[1] = this.createServiceBuffer.accessStrategy.rejectedAttributes.uid[1].filter((item) => item._id !== user._id)
      }
    },
    attributeHelper (scope, command, index) {
      if (scope === "create") {
        if (command === "add") {
          this.createServiceBuffer.attributeList.push({ name: "", value: "" })
        } else if (command === "remove") {
          this.createServiceBuffer.attributeList.splice(index, 1)
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
