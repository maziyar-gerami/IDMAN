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
      persianRex: null
    }
  },
  mounted () {
    this.persianDate = require("persian-date/dist/persian-date")
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
                    // document.getElementById("editSelect" + String(res.data.data.extraInfo.dailyAccess[i].weekDay)).checked = true
                    vm.editDailyAccessCheckbox[res.data.data.extraInfo.dailyAccess[i].weekDay] = true
                    console.log(vm.editDailyAccessCheckbox)
                    document.getElementById("editServiceBuffer.dailyAccess.start" + String(res.data.data.extraInfo.dailyAccess[i].weekDay)).value =
                      vm.enNumToFaNum(String(res.data.data.extraInfo.dailyAccess[i].period.from.hour)) + ":" + vm.enNumToFaNum(String(res.data.data.extraInfo.dailyAccess[i].period.from.minute))
                    document.getElementById("editServiceBuffer.dailyAccess.end" + String(res.data.data.extraInfo.dailyAccess[i].weekDay)).value =
                      vm.enNumToFaNum(String(res.data.data.extraInfo.dailyAccess[i].period.to.hour)) + ":" + vm.enNumToFaNum(String(res.data.data.extraInfo.dailyAccess[i].period.to.minute))
                  }
                  if (res.data.data.extraInfo.dailyAccess.length === 7) {
                    // document.getElementById("editSelectAll").checked = true
                    vm.editDailyAccessCheckAll = true
                    console.log(vm.editDailyAccessCheckAll)
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
              document.getElementById("createServiceBuffer.startDate").value = dayWrapper.toCalendar("persian").format("dddd DD MMMM YYYY  HH:mm  a")
            }
            if (typeof res.data.data.accessStrategy.endingDateTime !== "undefined") {
              const seTime = res.data.data.accessStrategy.endingDateTime
              vm.persianDate.toCalendar("gregorian")
              // eslint-disable-next-line new-cap
              const dayWrapper = new vm.persianDate([seTime.substring(0, 4), seTime.substring(5, 7), seTime.substring(8, 10),
                seTime.substring(11, 13), seTime.substring(14, 16), seTime.substring(17, 19), seTime.substring(20, 23)])
              document.getElementById("createServiceBuffer.endDate").value = dayWrapper.toCalendar("persian").format("dddd DD MMMM YYYY  HH:mm  a")
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
        }).catch((e) => {
          console.log(e)
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

          this.accessStrategy.startingDateTime = dateStartFinal + "T" + timeStart
          this.accessStrategy.endingDateTime = dateEndFinal + "T" + timeEnd
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

          this.accessStrategy.startingDateTime = dateStartFinal + "T" + timeStart
          this.accessStrategy.endingDateTime = dateEndFinal + "T" + timeEnd
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
            if (res.data.status.code === 200) {
              vm.createServiceErrors.metadataLocation = ""
              vm.createServiceBuffer.metadataLocation = res.data.data
              if (vm.createServiceBuffer.logoFile !== null) {
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
                    if (errorCount > 0) {
                      vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
                    } else {
                      vm.servicesRequestMaster("createService")
                    }
                  }
                  vm.createServiceLoader = false
                }).catch(() => {
                  vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                  vm.createServiceLoader = false
                })
              } else {
                if (errorCount > 0) {
                  vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
                } else {
                  vm.servicesRequestMaster("createService")
                }
              }
            } else {
              vm.createServiceErrors.metadataLocation = "p-invalid"
              errorCount += 1
            }
            vm.createServiceLoader = false
          }).catch(() => {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createServiceErrors.metadataLocation = "p-invalid"
            errorCount += 1
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
      } else {
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
            if (res.data.status.code === 200) {
              vm.createServiceBuffer.logo = res.data.data
              if (errorCount > 0) {
                vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
              } else {
                vm.servicesRequestMaster("createService")
              }
            }
            vm.createServiceLoader = false
          }).catch(() => {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.createServiceLoader = false
          })
        } else {
          if (errorCount > 0) {
            this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
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

      if (this.editServiceBuffer.serviceType === "SAML") {
        if (this.editServiceBuffer.metadataLocationOption === "address") {
          if (this.editServiceBuffer.metadataLocation === "") {
            this.editServiceErrors.metadataLocation = "p-invalid"
            errorCount += 1
          } else {
            this.editServiceErrors.metadataLocation = ""
          }
        } else if (this.editServiceBuffer.metadataLocationOption === "file") {
          const metadataFileBodyFormData = new FormData()
          metadataFileBodyFormData.append("file", this.editServiceBuffer.metadataFile)
          const vm = this
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
              if (vm.editServiceBuffer.logoFile !== null) {
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
                    if (errorCount > 0) {
                      vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
                    } else {
                      vm.servicesRequestMaster("editService")
                    }
                  }
                  vm.editServiceLoader = false
                }).catch(() => {
                  vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
                  vm.editServiceLoader = false
                })
              } else {
                if (errorCount > 0) {
                  vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
                } else {
                  vm.servicesRequestMaster("editService")
                }
              }
            } else {
              vm.editServiceErrors.metadataLocation = "p-invalid"
              errorCount += 1
            }
            vm.editServiceLoader = false
          }).catch(() => {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editServiceErrors.metadataLocation = "p-invalid"
            errorCount += 1
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
      } else {
        if (this.editServiceBuffer.logoFile !== null) {
          const logoFileBodyFormData = new FormData()
          logoFileBodyFormData.append("file", this.editServiceBuffer.logoFile)
          const vm = this
          this.editServiceLoader = true
          this.axios({
            url: "/api/services/icon",
            method: "POST",
            headers: { "Content-Type": "multipart/form-data" },
            data: logoFileBodyFormData
          }).then((res) => {
            if (res.data.status.code === 200) {
              vm.editServiceBuffer.logo = res.data.data
              if (errorCount > 0) {
                vm.alertPromptMaster(vm.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
              } else {
                vm.servicesRequestMaster("editService")
              }
            }
            vm.editServiceLoader = false
          }).catch(() => {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.editServiceLoader = false
          })
        } else {
          if (errorCount > 0) {
            this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
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
