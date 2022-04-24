<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h3>{{ $t("ticketing") }}</h3>
        <div v-if="$store.state.accessLevel === 2 || $store.state.accessLevel === 4">
          <TabView v-if="supporterListFlag" v-model:activeIndex="tabActiveIndex">
            <TabPanel :header="$t('myOpenTickets')">
              <Toolbar>
                <template #start>
                  <Button :label="$t('close')" icon="bx bx-check bx-sm" class="p-button-success mx-1" @click="ticketingToolbar('inbox', 'close')" />
                  <Button :label="$t('delete')" icon="bx bxs-trash bx-sm" class="p-button-danger mx-1" @click="ticketingToolbar('inbox', 'delete')" />
                </template>
                <template #end>
                  <span class="p-input-icon-left">
                    <i class="pi pi-times-circle" @click="removeFilters('inbox', 'date')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                    <InputText id="inboxFilters.date" type="text" :placeholder="$t('date')" class="datePickerFa" />
                  </span>
                  <Button :label="$t('filter')" class="p-button mx-1" @click="inboxRequestMaster('getInbox')" />
                </template>
              </Toolbar>
              <DataTable :value="inbox" filterDisplay="menu" dataKey="_id" :rows="rowsPerPageInbox" :loading="loadingInbox" scrollHeight="50vh"
              class="p-datatable-gridlines" :rowHover="true" responsiveLayout="scroll" scrollDirection="vertical"
              :filters="filtersInbox" v-model:selection="selectedTicketsInbox" :scrollable="false">
                <template #header>
                  <div class="flex justify-content-between flex-column sm:flex-row">
                    <div></div>
                    <Paginator v-model:rows="rowsPerPageInbox" v-model:totalRecords="totalRecordsCountInbox" @page="onPaginatorEvent('inbox', $event)" :rowsPerPageOptions="[10,20,50,100,500]"></Paginator>
                    <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="removeFilters('inbox', 'all')" />
                  </div>
                </template>
                <template #empty>
                  <div class="text-right">
                    {{ $t("noTicketFound") }}
                  </div>
                </template>
                <template #loading>
                  <div class="text-right">
                    {{ $t("loadingTickets") }}
                  </div>
                </template>
                <Column selectionMode="multiple" bodyClass="text-center" style="width:5rem;"></Column>
                <Column field="isUnanswered" bodyClass="text-center" style="width:5rem;">
                  <template #body="{data}">
                    <i v-if="data.isUnanswered" class="bx bxs-envelope bx-sm" style="color: #007bff;" v-tooltip.top="$t('unanswered')" />
                    <i v-else class="bx bxs-envelope-open bx-sm" v-tooltip.top="$t('answered')" />
                  </template>
                </Column>
                <Column field="_id" :header="$t('id')" bodyClass="text-center" style="flex: 0 0 5rem">
                  <template #body="{data}">
                    {{ data._id }}
                  </template>
                  <template #filter="{filterCallback}">
                    <InputText type="text" v-model="inboxFilters._id" @keydown.enter="filterCallback(); inboxRequestMaster('getInbox')" class="p-column-filter" :placeholder="$t('id')"/>
                  </template>
                  <template #filterapply="{filterCallback}">
                    <Button type="button" icon="bx bx-check bx-sm" @click="filterCallback(); inboxRequestMaster('getInbox')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                  </template>
                  <template #filterclear="{filterCallback}">
                    <Button type="button" icon="bx bx-x bx-sm" @click="filterCallback(); removeFilters('inbox', '_id')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                  </template>
                </Column>
                <Column field="subject" :header="$t('subject')" bodyClass="text-center" style="flex: 0 0 12rem">
                  <template #body="{data}">
                    {{ data.subject }}
                  </template>
                </Column>
                <Column field="from" :header="$t('from')" bodyClass="text-center" style="flex: 0 0 7rem">
                  <template #body="{data}">
                    {{ data.from }}
                  </template>
                  <template #filter="{filterCallback}">
                    <InputText type="text" v-model="inboxFilters.from" @keydown.enter="filterCallback(); inboxRequestMaster('getInbox')" class="p-column-filter" :placeholder="$t('from')"/>
                  </template>
                  <template #filterapply="{filterCallback}">
                    <Button type="button" icon="bx bx-check bx-sm" @click="filterCallback(); inboxRequestMaster('getInbox')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                  </template>
                  <template #filterclear="{filterCallback}">
                    <Button type="button" icon="bx bx-x bx-sm" @click="filterCallback(); removeFilters('inbox', 'from')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                  </template>
                </Column>
                <Column field="dateString" :header="$t('date')" bodyClass="text-center" style="flex: 0 0 7rem">
                  <template #body="{data}">
                    {{ data.dateString }}
                  </template>
                </Column>
                <Column field="timeString" :header="$t('time')" bodyClass="text-center" style="flex: 0 0 7rem">
                  <template #body="{data}">
                    {{ data.timeString }}
                  </template>
                </Column>
                <Column bodyStyle="display: flex;" bodyClass="flex justify-content-evenly flex-wrap card-container text-center w-full" style="flex: 0 0 10rem">
                  <template #body="{data}">
                    <div class="flex align-items-center justify-content-center">
                      <Button icon="bx bx-reply bx-sm" class="p-button-rounded p-button-outlined mx-1" @click="getTicket('inbox', data._id)" v-tooltip.top="$t('reply')" />
                    </div>
                    <div class="flex align-items-center justify-content-center">
                      <Button icon="bx bxs-trash bx-sm" class="p-button-rounded p-button-danger p-button-outlined mx-1" @click="deleteTicket('inbox', data._id)" v-tooltip.top="$t('delete')" />
                    </div>
                  </template>
                </Column>
              </DataTable>
            </TabPanel>
            <TabPanel :header="$t('ticketsList')">
              <Toolbar>
                <template #start>
                  <Button :label="$t('close')" icon="bx bx-check bx-sm" class="p-button-success mx-1" @click="ticketingToolbar('tickets', 'close')" />
                  <Button :label="$t('delete')" icon="bx bxs-trash bx-sm" class="p-button-danger mx-1" @click="ticketingToolbar('tickets', 'delete')" />
                </template>
                <template #end>
                  <span class="p-input-icon-left">
                    <i class="pi pi-times-circle" @click="removeFilters('tickets', 'date')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                    <InputText id="ticketsFilters.date" type="text" :placeholder="$t('date')" class="datePickerFa" />
                  </span>
                  <Button :label="$t('filter')" class="p-button mx-1" @click="ticketsRequestMaster('getTickets')" />
                </template>
              </Toolbar>
              <DataTable :value="tickets" filterDisplay="menu" dataKey="_id" :rows="rowsPerPageTickets" :loading="loadingTickets" scrollHeight="50vh"
              class="p-datatable-gridlines" :rowHover="true" responsiveLayout="scroll" scrollDirection="vertical"
              :filters="filtersTickets" v-model:selection="selectedTickets" :scrollable="false">
                <template #header>
                  <div class="flex justify-content-between flex-column sm:flex-row">
                    <div>
                      <div class="p-inputgroup mx-1">
                        <Button icon="pi pi-times" class="p-button-danger" @click="removeFilters('tickets', 'status')" v-tooltip.top="$t('removeFilter')" />
                        <Dropdown v-model="ticketsFilters.status" :options="ticketsStatusFilter" @change="ticketsRequestMaster('getTickets')" optionLabel="name" :placeholder="$t('status')" />
                      </div>
                    </div>
                    <div>
                      <Paginator v-model:rows="rowsPerPageTickets" v-model:totalRecords="totalRecordsCountTickets" @page="onPaginatorEvent('tickets', $event)" :rowsPerPageOptions="[10,20,50,100,500]"></Paginator>
                    </div>
                    <div>
                      <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="removeFilters('tickets', 'all')" />
                    </div>
                  </div>
                </template>
                <template #empty>
                  <div class="text-right">
                    {{ $t("noTicketFound") }}
                  </div>
                </template>
                <template #loading>
                  <div class="text-right">
                    {{ $t("loadingTickets") }}
                  </div>
                </template>
                <Column selectionMode="multiple" bodyClass="text-center" style="width:5rem;"></Column>
                <Column field="_id" :header="$t('id')" bodyClass="text-center" style="flex: 0 0 5rem">
                  <template #body="{data}">
                    {{ data._id }}
                  </template>
                  <template #filter="{filterCallback}">
                    <InputText type="text" v-model="ticketsFilters._id" @keydown.enter="filterCallback(); ticketsRequestMaster('getTickets')" class="p-column-filter" :placeholder="$t('id')"/>
                  </template>
                  <template #filterapply="{filterCallback}">
                    <Button type="button" icon="bx bx-check bx-sm" @click="filterCallback(); ticketsRequestMaster('getTickets')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                  </template>
                  <template #filterclear="{filterCallback}">
                    <Button type="button" icon="bx bx-x bx-sm" @click="filterCallback(); removeFilters('tickets', '_id')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                  </template>
                </Column>
                <Column field="subject" :header="$t('subject')" bodyClass="text-center" style="flex: 0 0 12rem">
                  <template #body="{data}">
                    {{ data.subject }}
                  </template>
                </Column>
                <Column field="status" :header="$t('status')" bodyClass="text-center" style="flex: 0 0 5rem">
                  <template #body="{data}">
                    <div v-if="data.status === 0">{{ $t("pending") }}</div>
                    <div v-else-if="data.status === 1">{{ $t("open") }}</div>
                    <div v-else-if="data.status === 2">{{ $t("closed") }}</div>
                  </template>
                </Column>
                <Column field="from" :header="$t('from')" bodyClass="text-center" style="flex: 0 0 7rem">
                  <template #body="{data}">
                    {{ data.from }}
                  </template>
                  <template #filter="{filterCallback}">
                    <InputText type="text" v-model="ticketsFilters.from" @keydown.enter="filterCallback(); ticketsRequestMaster('getTickets')" class="p-column-filter" :placeholder="$t('from')"/>
                  </template>
                  <template #filterapply="{filterCallback}">
                    <Button type="button" icon="bx bx-check bx-sm" @click="filterCallback(); ticketsRequestMaster('getTickets')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                  </template>
                  <template #filterclear="{filterCallback}">
                    <Button type="button" icon="bx bx-x bx-sm" @click="filterCallback(); removeFilters('tickets', 'from')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                  </template>
                </Column>
                <Column field="dateString" :header="$t('date')" bodyClass="text-center" style="flex: 0 0 7rem">
                  <template #body="{data}">
                    {{ data.dateString }}
                  </template>
                </Column>
                <Column field="timeString" :header="$t('time')" bodyClass="text-center" style="flex: 0 0 7rem">
                  <template #body="{data}">
                    {{ data.timeString }}
                  </template>
                </Column>
                <Column bodyStyle="display: flex;" bodyClass="flex justify-content-evenly flex-wrap card-container text-center w-full" style="flex: 0 0 10rem">
                  <template #body="{data}">
                    <div class="flex align-items-center justify-content-center">
                      <Button icon="bx bx-reply bx-sm" class="p-button-rounded p-button-outlined mx-1" @click="getTicket('tickets', data._id)" v-tooltip.top="$t('reply')" />
                    </div>
                    <div class="flex align-items-center justify-content-center">
                      <Button icon="bx bxs-trash bx-sm" class="p-button-rounded p-button-danger p-button-outlined mx-1" @click="deleteTicket('tickets', data._id)" v-tooltip.top="$t('delete')" />
                    </div>
                  </template>
                </Column>
              </DataTable>
            </TabPanel>
            <TabPanel v-if="$store.state.accessLevel === 4" :header="$t('ticketArchives')">
              <Toolbar>
                <template #start>
                  <span class="p-input-icon-left">
                    <i class="pi pi-times-circle" @click="removeFilters('archives', 'date')" v-tooltip.top="$t('removeFilter')" style="color: red; cursor: pointer;" />
                    <InputText id="archivesFilters.date" type="text" :placeholder="$t('date')" class="datePickerFa" />
                  </span>
                  <Button :label="$t('filter')" class="p-button mx-1" @click="archivesRequestMaster('getArchives')" />
                </template>
              </Toolbar>
              <DataTable :value="archives" filterDisplay="menu" dataKey="_id" :rows="rowsPerPageArchives" :loading="loadingArchives" scrollHeight="50vh"
              class="p-datatable-gridlines" :rowHover="true" responsiveLayout="scroll" scrollDirection="vertical" :filters="filtersArchives" :scrollable="false">
                <template #header>
                  <div class="flex justify-content-between flex-column sm:flex-row">
                    <div></div>
                    <Paginator v-model:rows="rowsPerPageArchives" v-model:totalRecords="totalRecordsCountArchives" @page="onPaginatorEvent('archives', $event)" :rowsPerPageOptions="[10,20,50,100,500]"></Paginator>
                    <Button icon="pi pi-filter-slash" v-tooltip.top="$t('removeFilters')" class="p-button-danger mb-2 mx-1" @click="removeFilters('archives', 'all')" />
                  </div>
                </template>
                <template #empty>
                  <div class="text-right">
                    {{ $t("noTicketFound") }}
                  </div>
                </template>
                <template #loading>
                  <div class="text-right">
                    {{ $t("loadingTickets") }}
                  </div>
                </template>
                <Column field="_id" :header="$t('id')" bodyClass="text-center" style="flex: 0 0 5rem">
                  <template #body="{data}">
                    {{ data._id }}
                  </template>
                  <template #filter="{filterCallback}">
                    <InputText type="text" v-model="archivesFilters._id" @keydown.enter="filterCallback(); archivesRequestMaster('getArchives')" class="p-column-filter" :placeholder="$t('id')"/>
                  </template>
                  <template #filterapply="{filterCallback}">
                    <Button type="button" icon="bx bx-check bx-sm" @click="filterCallback(); archivesRequestMaster('getArchives')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                  </template>
                  <template #filterclear="{filterCallback}">
                    <Button type="button" icon="bx bx-x bx-sm" @click="filterCallback(); removeFilters('archives', '_id')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                  </template>
                </Column>
                <Column field="subject" :header="$t('subject')" bodyClass="text-center" style="flex: 0 0 12rem">
                  <template #body="{data}">
                    {{ data.subject }}
                  </template>
                </Column>
                <Column field="status" :header="$t('status')" bodyClass="text-center" style="flex: 0 0 5rem">
                  <template #body="{data}">
                    <div v-if="data.status === 0">{{ $t("pending") }}</div>
                    <div v-else-if="data.status === 1">{{ $t("open") }}</div>
                    <div v-else-if="data.status === 2">{{ $t("closed") }}</div>
                  </template>
                </Column>
                <Column field="from" :header="$t('from')" bodyClass="text-center" style="flex: 0 0 7rem">
                  <template #body="{data}">
                    {{ data.from }}
                  </template>
                  <template #filter="{filterCallback}">
                    <InputText type="text" v-model="archivesFilters.from" @keydown.enter="filterCallback(); archivesRequestMaster('getArchives')" class="p-column-filter" :placeholder="$t('from')"/>
                  </template>
                  <template #filterapply="{filterCallback}">
                    <Button type="button" icon="bx bx-check bx-sm" @click="filterCallback(); archivesRequestMaster('getArchives')" v-tooltip.top="$t('applyFilter')" class="p-button-success"></Button>
                  </template>
                  <template #filterclear="{filterCallback}">
                    <Button type="button" icon="bx bx-x bx-sm" @click="filterCallback(); removeFilters('archives', 'from')" v-tooltip.top="$t('removeFilter')" class="p-button-danger"></Button>
                  </template>
                </Column>
                <Column field="dateString" :header="$t('date')" bodyClass="text-center" style="flex: 0 0 7rem">
                  <template #body="{data}">
                    {{ data.dateString }}
                  </template>
                </Column>
                <Column field="timeString" :header="$t('time')" bodyClass="text-center" style="flex: 0 0 7rem">
                  <template #body="{data}">
                    {{ data.timeString }}
                  </template>
                </Column>
                <Column bodyStyle="display: flex;" bodyClass="flex justify-content-evenly flex-wrap card-container text-center w-full" style="flex: 0 0 5rem">
                  <template #body="{data}">
                    <div class="flex align-items-center justify-content-center">
                      <Button icon="bx bx-info-circle bx-sm" class="p-button-rounded p-button-info p-button-outlined mx-1" @click="getTicket('archives', data._id)" v-tooltip.top="$t('description')" />
                    </div>
                  </template>
                </Column>
              </DataTable>
            </TabPanel>
          </TabView>
          <div v-else-if="supporterReplyFlag">
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketSupporter._id">{{ $t("id") }}</label>
                  <InputText id="replyTicketSupporter._id" type="text" v-model="replyTicketSupporter._id" disabled />
                </div>
              </div>
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketSupporter.subject">{{ $t("subject") }}</label>
                  <InputText id="replyTicketSupporter.subject" type="text" v-model="replyTicketSupporter.subject" disabled />
                </div>
              </div>
            </div>
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketSupporter.from">{{ $t("from") }}</label>
                  <InputText id="replyTicketSupporter.from" type="text" v-model="replyTicketSupporter.from" disabled />
                </div>
              </div>
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketSupporter.dateTimeString">{{ $t("createDateTime") }}</label>
                  <InputText id="replyTicketSupporter.dateTimeString" type="text" v-model="replyTicketSupporter.dateTimeString" disabled />
                </div>
              </div>
            </div>
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketSupporter.messages">{{ $t("messages") }}</label>
                  <div id="replyTicketSupporter.messages" class="mt-3 mb-3" style="overflow: auto; height: 40vh;">
                    <div v-for="msg in replyTicketSupporter.messages" :key="msg" class="grid w-full">
                      <div v-if="msg.close || msg.reOpen" class="w-full">
                        <div class="col-6"></div>
                        <div class="col-6 p-1 mt-3 alertMessage">
                          <small v-if="msg.reOpen">{{ $t("ticketingText1") }}{{ msg.reOpenTime.hours }}:{{ msg.reOpenTime.minutes }}:{{ msg.reOpenTime.seconds }} {{ msg.reOpenTime.year }}/{{ msg.reOpenTime.month }}/{{ msg.reOpenTime.day }}{{ $t("ticketingText2") }}{{ msg.from }}{{ $t("ticketingText3") }}</small>
                          <small v-else>{{$t("ticketingText1")}}{{msg.closeTime.hours}}:{{msg.closeTime.minutes}}:{{msg.closeTime.seconds}} {{msg.closeTime.year}}/{{msg.closeTime.month}}/{{msg.closeTime.day}}{{$t("ticketingText2")}}{{msg.from}}{{$t("ticketingText4")}}</small>
                        </div>
                        <div class="col-2"></div>
                      </div>
                      <div v-else-if="msg.from === replyTicketSupporter.from" class="w-full">
                        <div class="col-6"></div>
                        <div class="col-6 p-2 mt-3 receivedMessage">
                          {{msg.body}}
                          <br><br>
                          <small>{{msg.creationTime.hours}}:{{msg.creationTime.minutes}}:{{msg.creationTime.seconds}} {{msg.creationTime.year}}/{{msg.creationTime.month}}/{{msg.creationTime.day}} ({{msg.fromDisplayName}})</small>
                        </div>
                      </div>
                      <div v-else class="w-full">
                        <div class="col-6 p-2 mt-3 sentMessage">
                          {{msg.body}}
                          <br><br>
                          <small>{{msg.creationTime.hours}}:{{msg.creationTime.minutes}}:{{msg.creationTime.seconds}} {{msg.creationTime.year}}/{{msg.creationTime.month}}/{{msg.creationTime.day}} ({{msg.fromDisplayName}})</small>
                        </div>
                        <div class="col-6"></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketSupporter.replyBody">{{ $t("reply") }}</label>
                  <Textarea id="replyTicketSupporter.replyBody" v-model="replyBodySupporter" :autoResize="true" rows="3" />
                </div>
              </div>
            </div>
            <Button :label="$t('reply&close')" class="p-button-success mt-3 mx-1" @click="replyTicket('inbox', 'reply&close')" />
            <Button :label="$t('reply')" class="p-button-success mt-3 mx-1" @click="replyTicket('inbox', 'reply')" />
            <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('inbox')" />
          </div>
          <div v-else-if="supporterShowFlag">
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="showTicketSupporter._id">{{ $t("id") }}</label>
                  <InputText id="showTicketSupporter._id" type="text" v-model="showTicketSupporter._id" disabled />
                </div>
              </div>
              <div class="field col">
                <div class="field p-fluid">
                  <label for="showTicketSupporter.subject">{{ $t("subject") }}</label>
                  <InputText id="showTicketSupporter.subject" type="text" v-model="showTicketSupporter.subject" disabled />
                </div>
              </div>
            </div>
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="showTicketSupporter.from">{{ $t("from") }}</label>
                  <InputText id="showTicketSupporter.from" type="text" v-model="showTicketSupporter.from" disabled />
                </div>
              </div>
              <div class="field col">
                <div class="field p-fluid">
                  <label for="showTicketSupporter.dateTimeString">{{ $t("createDateTime") }}</label>
                  <InputText id="showTicketSupporter.dateTimeString" type="text" v-model="showTicketSupporter.dateTimeString" disabled />
                </div>
              </div>
            </div>
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="showTicketSupporter.messages">{{ $t("messages") }}</label>
                  <div id="showTicketSupporter.messages" class="mt-3 mb-3" style="overflow: auto; height: 40vh;">
                    <div v-for="msg in showTicketSupporter.messages" :key="msg" class="grid w-full">
                      <div v-if="msg.close || msg.reOpen" class="w-full">
                        <div class="col-6"></div>
                        <div class="col-6 p-1 mt-3 alertMessage">
                          <small v-if="msg.reOpen">{{ $t("ticketingText1") }}{{ msg.reOpenTime.hours }}:{{ msg.reOpenTime.minutes }}:{{ msg.reOpenTime.seconds }} {{ msg.reOpenTime.year }}/{{ msg.reOpenTime.month }}/{{ msg.reOpenTime.day }}{{ $t("ticketingText2") }}{{ msg.from }}{{ $t("ticketingText3") }}</small>
                          <small v-else>{{$t("ticketingText1")}}{{msg.closeTime.hours}}:{{msg.closeTime.minutes}}:{{msg.closeTime.seconds}} {{msg.closeTime.year}}/{{msg.closeTime.month}}/{{msg.closeTime.day}}{{$t("ticketingText2")}}{{msg.from}}{{$t("ticketingText4")}}</small>
                        </div>
                        <div class="col-2"></div>
                      </div>
                      <div v-else-if="msg.from === showTicketSupporter.from" class="w-full">
                        <div class="col-6"></div>
                        <div class="col-6 p-2 mt-3 receivedMessage">
                          {{msg.body}}
                          <br><br>
                          <small>{{msg.creationTime.hours}}:{{msg.creationTime.minutes}}:{{msg.creationTime.seconds}} {{msg.creationTime.year}}/{{msg.creationTime.month}}/{{msg.creationTime.day}} ({{msg.fromDisplayName}})</small>
                        </div>
                      </div>
                      <div v-else class="w-full">
                        <div class="col-6 p-2 mt-3 sentMessage">
                          {{msg.body}}
                          <br><br>
                          <small>{{msg.creationTime.hours}}:{{msg.creationTime.minutes}}:{{msg.creationTime.seconds}} {{msg.creationTime.year}}/{{msg.creationTime.month}}/{{msg.creationTime.day}} ({{msg.fromDisplayName}})</small>
                        </div>
                        <div class="col-6"></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('archives')" />
          </div>
        </div>
        <div v-else-if="$store.state.accessLevel === 1 || $store.state.accessLevel === 3">
          <div v-if="userListFlag">
            <Toolbar>
              <template #start>
                <Button :label="$t('new')" icon="bx bx-plus bx-sm" class="p-button-success mx-1" @click="ticketingToolbar('userTickets', 'create')" />
                <Button :label="$t('delete')" icon="bx bxs-trash bx-sm" class="p-button-danger mx-1" @click="ticketingToolbar('userTickets', 'delete')" />
              </template>
            </Toolbar>
            <DataTable :value="userTickets" filterDisplay="menu" dataKey="_id" :rows="rowsPerPageUserTickets" :loading="loadingUserTickets" scrollHeight="50vh"
            class="p-datatable-gridlines" :rowHover="true" responsiveLayout="scroll" scrollDirection="vertical"
            :filters="filtersUserTickets" v-model:selection="selectedTicketsUserTickets" :scrollable="false">
              <template #header>
                <div class="flex justify-content-between flex-column sm:flex-row">
                  <div></div>
                  <Paginator v-model:rows="rowsPerPageUserTickets" v-model:totalRecords="totalRecordsCountUserTickets" @page="onPaginatorEvent('userTickets', $event)" :rowsPerPageOptions="[10,20,50,100,500]"></Paginator>
                  <div></div>
                </div>
              </template>
              <template #empty>
                <div class="text-right">
                  {{ $t("noTicketFound") }}
                </div>
              </template>
              <template #loading>
                <div class="text-right">
                  {{ $t("loadingTickets") }}
                </div>
              </template>
              <Column selectionMode="multiple" bodyClass="text-center" style="width:5rem;"></Column>
              <Column field="isUnanswered" bodyClass="text-center" style="width:5rem;">
                <template #body="{data}">
                  <i v-if="data.isUnanswered" class="bx bxs-envelope bx-sm" style="color: #007bff;" v-tooltip.top="$t('unanswered')" />
                  <i v-else class="bx bxs-envelope-open bx-sm" v-tooltip.top="$t('answered')" />
                </template>
              </Column>
              <Column field="_id" :header="$t('id')" bodyClass="text-center" style="flex: 0 0 5rem">
                <template #body="{data}">
                  {{ data._id }}
                </template>
              </Column>
              <Column field="subject" :header="$t('subject')" bodyClass="text-center" style="flex: 0 0 12rem">
                <template #body="{data}">
                  {{ data.subject }}
                </template>
              </Column>
              <Column field="status" :header="$t('status')" bodyClass="text-center" style="flex: 0 0 5rem">
                <template #body="{data}">
                  <div v-if="data.status === 0">{{ $t("pending") }}</div>
                  <div v-else-if="data.status === 1">{{ $t("open") }}</div>
                  <div v-else-if="data.status === 2">{{ $t("closed") }}</div>
                </template>
              </Column>
              <Column field="from" :header="$t('from')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  {{ data.from }}
                </template>
              </Column>
              <Column field="dateString" :header="$t('date')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  {{ data.dateString }}
                </template>
              </Column>
              <Column field="timeString" :header="$t('time')" bodyClass="text-center" style="flex: 0 0 7rem">
                <template #body="{data}">
                  {{ data.timeString }}
                </template>
              </Column>
              <Column bodyStyle="display: flex;" bodyClass="flex justify-content-evenly flex-wrap card-container text-center w-full" style="flex: 0 0 10rem">
                <template #body="{data}">
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="bx bx-reply bx-sm" class="p-button-rounded p-button-outlined mx-1" @click="getTicket('userTickets', data._id)" v-tooltip.top="$t('reply')" />
                  </div>
                  <div class="flex align-items-center justify-content-center">
                    <Button icon="bx bxs-trash bx-sm" class="p-button-rounded p-button-danger p-button-outlined mx-1" @click="deleteTicket('userTickets', data._id)" v-tooltip.top="$t('delete')" />
                  </div>
                </template>
              </Column>
            </DataTable>
          </div>
          <div v-else-if="userReplyFlag">
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketUser._id">{{ $t("id") }}</label>
                  <InputText id="replyTicketUser._id" type="text" v-model="replyTicketUser._id" disabled />
                </div>
              </div>
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketUser.subject">{{ $t("subject") }}</label>
                  <InputText id="replyTicketUser.subject" type="text" v-model="replyTicketUser.subject" disabled />
                </div>
              </div>
            </div>
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketUser.from">{{ $t("from") }}</label>
                  <InputText id="replyTicketUser.from" type="text" v-model="replyTicketUser.from" disabled />
                </div>
              </div>
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketUser.dateTimeString">{{ $t("createDateTime") }}</label>
                  <InputText id="replyTicketUser.dateTimeString" type="text" v-model="replyTicketUser.dateTimeString" disabled />
                </div>
              </div>
            </div>
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketUser.messages">{{ $t("messages") }}</label>
                  <div id="replyTicketUser.messages" class="mt-3 mb-3" style="overflow: auto; height: 40vh;">
                    <div v-for="msg in replyTicketUser.messages" :key="msg" class="grid w-full">
                      <div v-if="msg.close || msg.reOpen" class="w-full">
                        <div class="col-6"></div>
                        <div class="col-6 p-1 mt-3 alertMessage">
                          <small v-if="msg.reOpen">{{ticketingText1}}{{msg.reOpenTime.hours}}:{{msg.reOpenTime.minutes}}:{{msg.reOpenTime.seconds}} {{msg.reOpenTime.year}}/{{msg.reOpenTime.month}}/{{msg.reOpenTime.day}}{{ticketingText2}}{{msg.from}}{{ticketingText3}}</small>
                          <small v-else>{{ticketingText1}}{{msg.closeTime.hours}}:{{msg.closeTime.minutes}}:{{msg.closeTime.seconds}} {{msg.closeTime.year}}/{{msg.closeTime.month}}/{{msg.closeTime.day}}{{ticketingText2}}{{msg.from}}{{ticketingText4}}</small>
                        </div>
                        <div class="col-2"></div>
                      </div>
                      <div v-else-if="msg.from === replyTicketUser.from" class="w-full">
                        <div class="col-6"></div>
                        <div class="col-6 p-2 mt-3 receivedMessage">
                          {{msg.body}}
                          <br><br>
                          <small>{{msg.creationTime.hours}}:{{msg.creationTime.minutes}}:{{msg.creationTime.seconds}} {{msg.creationTime.year}}/{{msg.creationTime.month}}/{{msg.creationTime.day}} ({{msg.fromDisplayName}})</small>
                        </div>
                      </div>
                      <div v-else class="w-full">
                        <div class="col-6 p-2 mt-3 sentMessage">
                          {{msg.body}}
                          <br><br>
                          <small>{{msg.creationTime.hours}}:{{msg.creationTime.minutes}}:{{msg.creationTime.seconds}} {{msg.creationTime.year}}/{{msg.creationTime.month}}/{{msg.creationTime.day}} ({{msg.fromDisplayName}})</small>
                        </div>
                        <div class="col-6"></div>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="replyTicketUser.replyBody">{{ $t("reply") }}</label>
                  <Textarea id="replyTicketUser.replyBody" v-model="replyBodyUser" :autoResize="true" rows="3" />
                </div>
              </div>
            </div>
            <Button :label="$t('reply')" class="p-button-success mt-3 mx-1" @click="replyTicket('userTickets', 'reply')" />
            <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('userTickets')" />
          </div>
          <div v-else-if="userCreateFlag">
            <div class="formgrid grid">
              <div class="field col-6">
                <div class="field p-fluid">
                  <label for="createSubjectUser">{{ $t("subject") }}</label>
                  <InputText id="createSubjectUser" type="text" v-model="createSubjectUser" />
                </div>
              </div>
            </div>
            <div class="formgrid grid">
              <div class="field col">
                <div class="field p-fluid">
                  <label for="createBodyUser">{{ $t("message") }}</label>
                  <Textarea id="createBodyUser" v-model="createBodyUser" :autoResize="true" rows="3" />
                </div>
              </div>
            </div>
            <Button :label="$t('confirm')" class="p-button-success mt-3 mx-1" @click="createTicket()" />
            <Button :label="$t('back')" class="p-button-danger mt-3 mx-1" @click="resetState('userTickets')" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { FilterMatchMode } from "primevue/api"
import "persian-datepicker/dist/js/persian-datepicker"
import iziToast from "@/assets/scripts/iziToast.min.js"

export default {
  name: "Ticketing",
  data () {
    return {
      inbox: [],
      tickets: [],
      archives: [],
      userTickets: [],
      selectedTicketsInbox: [],
      selectedTickets: [],
      selectedTicketsUserTickets: [],
      replyTicketSupporter: {
        _id: "",
        from: "",
        subject: "",
        dateTimeString: "",
        messages: []
      },
      showTicketSupporter: {
        _id: "",
        from: "",
        subject: "",
        dateTimeString: "",
        messages: []
      },
      replyTicketUser: {
        _id: "",
        from: "",
        subject: "",
        dateTimeString: "",
        messages: []
      },
      inboxFilters: {
        _id: "",
        date: "",
        from: ""
      },
      ticketsFilters: {
        _id: "",
        date: "",
        status: {},
        from: ""
      },
      archivesFilters: {
        _id: "",
        date: "",
        from: ""
      },
      ticketsStatusFilter: [
        {
          id: "0",
          name: this.$t("pending")
        },
        {
          id: "1",
          name: this.$t("open")
        },
        {
          id: "2",
          name: this.$t("closed")
        },
        {
          id: "",
          name: this.$t("all")
        }
      ],
      toolbarBuffer: "",
      replyBodySupporter: "",
      replyBodyUser: "",
      createSubjectUser: "",
      createBodyUser: "",
      rowsPerPageInbox: 20,
      newPageNumberInbox: 1,
      totalRecordsCountInbox: 20,
      rowsPerPageTickets: 20,
      newPageNumberTickets: 1,
      totalRecordsCountTickets: 20,
      rowsPerPageArchives: 20,
      newPageNumberArchives: 1,
      totalRecordsCountArchives: 20,
      rowsPerPageUserTickets: 20,
      newPageNumberUserTickets: 1,
      totalRecordsCountUserTickets: 20,
      tabActiveIndex: 0,
      loadingInbox: false,
      loadingTickets: false,
      loadingArchives: false,
      loadingUserTickets: false,
      supporterListFlag: true,
      userListFlag: true,
      supporterReplyFlag: false,
      supporterShowFlag: false,
      userReplyFlag: false,
      userCreateFlag: false,
      filtersInbox: null,
      filtersTickets: null,
      filtersArchives: null
    }
  },
  mounted () {
    // eslint-disable-next-line no-undef
    jQuery(".datePickerFa").pDatepicker({
      inline: false,
      format: "DD MMMM YYYY",
      viewMode: "day",
      initialValue: false,
      initialValueType: "persian",
      autoClose: true,
      position: "auto",
      altFormat: "lll",
      altField: ".alt-field",
      onlyTimePicker: false,
      onlySelectOnDate: true,
      calendarType: "persian",
      inputDelay: 800,
      observer: false,
      calendar: {
        persian: {
          locale: "fa",
          showHint: true,
          leapYearMode: "algorithmic"
        },
        gregorian: {
          locale: "en",
          showHint: true
        }
      },
      navigator: {
        enabled: true,
        scroll: {
          enabled: true
        },
        text: {
          btnNextText: "<",
          btnPrevText: ">"
        }
      },
      toolbox: {
        enabled: true,
        calendarSwitch: {
          enabled: true,
          format: "MMMM"
        },
        todayButton: {
          enabled: true,
          text: {
            fa: "امروز",
            en: "Today"
          }
        },
        submitButton: {
          enabled: true,
          text: {
            fa: "تایید",
            en: "Submit"
          }
        },
        text: {
          btnToday: "امروز"
        }
      },
      timePicker: {
        enabled: false,
        step: 1,
        hour: {
          enabled: false,
          step: null
        },
        minute: {
          enabled: false,
          step: null
        },
        second: {
          enabled: false,
          step: null
        },
        meridian: {
          enabled: false
        }
      },
      dayPicker: {
        enabled: true,
        titleFormat: "YYYY MMMM"
      },
      monthPicker: {
        enabled: true,
        titleFormat: "YYYY"
      },
      yearPicker: {
        enabled: true,
        titleFormat: "YYYY"
      },
      responsive: true
    })
    this.initiateFilters()
    if (this.$store.state.accessLevel === 2 || this.$store.state.accessLevel === 4) {
      this.inboxRequestMaster("getInbox")
      this.ticketsRequestMaster("getTickets")
    } else if (this.$store.state.accessLevel === 1 || this.$store.state.accessLevel === 3) {
      this.userTicketsRequestMaster("getUserTickets")
    }
    if (this.$store.state.accessLevel === 4) {
      this.archivesRequestMaster("getArchives")
    }
  },
  methods: {
    initiateFilters () {
      this.filtersInbox = {
        _id: { value: null, matchMode: FilterMatchMode.CONTAINS },
        from: { value: null, matchMode: FilterMatchMode.CONTAINS }
      }
      this.filtersTickets = {
        _id: { value: null, matchMode: FilterMatchMode.CONTAINS },
        from: { value: null, matchMode: FilterMatchMode.CONTAINS }
      }
      this.filtersArchives = {
        _id: { value: null, matchMode: FilterMatchMode.CONTAINS },
        from: { value: null, matchMode: FilterMatchMode.CONTAINS }
      }
    },
    onPaginatorEvent (scale, event) {
      if (scale === "inbox") {
        this.newPageNumberInbox = event.page + 1
        this.rowsPerPageInbox = event.rows
        this.inboxRequestMaster("getInbox")
      } else if (scale === "tickets") {
        this.newPageNumberTickets = event.page + 1
        this.rowsPerPageTickets = event.rows
        this.ticketsRequestMaster("getTickets")
      } else if (scale === "archives") {
        this.newPageNumberArchives = event.page + 1
        this.rowsPerPageArchives = event.rows
        this.archivesRequestMaster("getArchives")
      } else if (scale === "userTickets") {
        this.newPageNumberUserTickets = event.page + 1
        this.rowsPerPageUserTickets = event.rows
        this.userTicketsRequestMaster("getUserTickets")
      }
    },
    inboxRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getInbox") {
        const unansweredTickets = []
        const answeredTickets = []
        this.inboxFilters.date = this.dateSerializer(document.getElementById("inboxFilters.date").value)
        this.loadingInbox = true
        this.axios({
          url: "/api/supporter/tickets/inbox/" + String(vm.newPageNumberInbox) + "/" + String(vm.rowsPerPageInbox),
          method: "GET",
          params: {
            id: vm.inboxFilters._id,
            from: vm.inboxFilters.from,
            date: vm.inboxFilters.date,
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            for (const i in res.data.data.ticketList) {
              if (res.data.data.ticketList[i].from === res.data.data.ticketList[i].lastFrom) {
                res.data.data.ticketList[i].isUnanswered = true
                unansweredTickets.push(res.data.data.ticketList[i])
              } else {
                res.data.data.ticketList[i].isUnanswered = false
                answeredTickets.push(res.data.data.ticketList[i])
              }
              res.data.data.ticketList[i].dateString = res.data.data.ticketList[i].creationDateTime.year + "/" + res.data.data.ticketList[i].creationDateTime.month + "/" + res.data.data.ticketList[i].creationDateTime.day
              res.data.data.ticketList[i].timeString = res.data.data.ticketList[i].creationDateTime.hours + ":" + res.data.data.ticketList[i].creationDateTime.minutes + ":" + res.data.data.ticketList[i].creationDateTime.seconds
            }
            vm.inbox = unansweredTickets.concat(answeredTickets)
            vm.totalRecordsCountInbox = res.data.data.size
            vm.loadingInbox = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingInbox = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingInbox = false
        })
      } else if (command === "getTicket") {
        this.loadingInbox = true
        this.axios({
          url: "/api/user/ticket/" + vm.toolbarBuffer,
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.replyTicketSupporter = res.data.data
            vm.replyTicketSupporter.dateTimeString = res.data.data.creationDateTime.year + "/" + res.data.data.creationDateTime.month + "/" + res.data.data.creationDateTime.day + " - " +
            res.data.data.creationDateTime.hours + ":" + res.data.data.creationDateTime.minutes + ":" + res.data.data.creationDateTime.seconds
            vm.loadingInbox = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingInbox = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingInbox = false
        })
      } else if (command === "replyTicket") {
        this.loadingInbox = true
        this.axios({
          url: "/api/user/ticket/reply/" + vm.replyTicketSupporter._id,
          method: "PUT",
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            message: vm.replyBodySupporter
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingInbox = false
            vm.getTicket("inbox", vm.replyTicketSupporter._id)
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingInbox = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingInbox = false
        })
      } else if (command === "reply&closeTicket") {
        this.loadingInbox = true
        this.axios({
          url: "/api/user/ticket/reply/" + vm.replyTicketSupporter._id,
          method: "PUT",
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            message: vm.replyBodySupporter,
            status: 2
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingInbox = false
            vm.resetState("inbox")
            vm.inboxRequestMaster("getInbox")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingInbox = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingInbox = false
        })
      } else if (command === "closeTickets") {
        const toolbarBufferList = []
        for (const x in this.selectedTicketsInbox) {
          toolbarBufferList.push(this.selectedTicketsInbox[x]._id)
        }
        this.loadingInbox = true
        this.axios({
          url: "/api/supporter/ticket/status/2",
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: toolbarBufferList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingInbox = false
            vm.inboxRequestMaster("getInbox")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingInbox = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingInbox = false
        })
      } else if (command === "deleteTicket") {
        const toolbarBufferList = [this.toolbarBuffer]
        this.loadingInbox = true
        this.axios({
          url: "/api/user/tickets",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: toolbarBufferList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingInbox = false
            vm.inboxRequestMaster("getInbox")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingInbox = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingInbox = false
        })
      } else if (command === "deleteTickets") {
        const toolbarBufferList = []
        for (const x in this.selectedTicketsInbox) {
          toolbarBufferList.push(this.selectedTicketsInbox[x]._id)
        }
        this.loadingInbox = true
        this.axios({
          url: "/api/user/tickets",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: toolbarBufferList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingInbox = false
            vm.inboxRequestMaster("getInbox")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingInbox = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingInbox = false
        })
      }
    },
    ticketsRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getTickets") {
        this.ticketsFilters.date = this.dateSerializer(document.getElementById("ticketsFilters.date").value)
        this.loadingTickets = true
        this.axios({
          url: "/api/supporter/tickets/" + String(vm.newPageNumberTickets) + "/" + String(vm.rowsPerPageTickets),
          method: "GET",
          params: {
            id: vm.ticketsFilters._id,
            from: vm.ticketsFilters.from,
            date: vm.ticketsFilters.date,
            status: vm.ticketsFilters.status.id,
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            for (const i in res.data.data.ticketList) {
              res.data.data.ticketList[i].dateString = res.data.data.ticketList[i].creationDateTime.year + "/" + res.data.data.ticketList[i].creationDateTime.month + "/" + res.data.data.ticketList[i].creationDateTime.day
              res.data.data.ticketList[i].timeString = res.data.data.ticketList[i].creationDateTime.hours + ":" + res.data.data.ticketList[i].creationDateTime.minutes + ":" + res.data.data.ticketList[i].creationDateTime.seconds
            }
            vm.tickets = res.data.data.ticketList
            vm.totalRecordsCountTickets = res.data.data.size
            vm.loadingTickets = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingTickets = false
        })
      } else if (command === "getTicket") {
        this.loadingTickets = true
        this.axios({
          url: "/api/user/ticket/" + vm.toolbarBuffer,
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.replyTicketSupporter = res.data.data
            vm.replyTicketSupporter.dateTimeString = res.data.data.creationDateTime.year + "/" + res.data.data.creationDateTime.month + "/" + res.data.data.creationDateTime.day + " - " +
            res.data.data.creationDateTime.hours + ":" + res.data.data.creationDateTime.minutes + ":" + res.data.data.creationDateTime.seconds
            vm.loadingTickets = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingTickets = false
        })
      } else if (command === "replyTicket") {
        this.loadingTickets = true
        this.axios({
          url: "/api/user/ticket/reply/" + vm.replyTicketSupporter._id,
          method: "PUT",
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            message: vm.replyBodySupporter
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingTickets = false
            vm.getTicket("tickets", vm.replyTicketSupporter._id)
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingTickets = false
        })
      } else if (command === "reply&closeTicket") {
        this.loadingTickets = true
        this.axios({
          url: "/api/user/ticket/reply/" + vm.replyTicketSupporter._id,
          method: "PUT",
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            message: vm.replyBodySupporter,
            status: 2
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingTickets = false
            vm.resetState("tickets")
            vm.ticketsRequestMaster("getTickets")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingTickets = false
        })
      } else if (command === "closeTickets") {
        const toolbarBufferList = []
        for (const x in this.selectedTickets) {
          toolbarBufferList.push(this.selectedTickets[x]._id)
        }
        this.loadingTickets = true
        this.axios({
          url: "/api/supporter/ticket/status/2",
          method: "PUT",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: toolbarBufferList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingTickets = false
            vm.ticketsRequestMaster("getTickets")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingTickets = false
        })
      } else if (command === "deleteTicket") {
        const toolbarBufferList = [this.toolbarBuffer]
        this.loadingTickets = true
        this.axios({
          url: "/api/user/tickets",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: toolbarBufferList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingTickets = false
            vm.ticketsRequestMaster("getTickets")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingTickets = false
        })
      } else if (command === "deleteTickets") {
        const toolbarBufferList = []
        for (const x in this.selectedTickets) {
          toolbarBufferList.push(this.selectedTickets[x]._id)
        }
        this.loadingTickets = true
        this.axios({
          url: "/api/user/tickets",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: toolbarBufferList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingTickets = false
            vm.ticketsRequestMaster("getTickets")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingTickets = false
        })
      }
    },
    archivesRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getArchives") {
        this.archivesFilters.date = this.dateSerializer(document.getElementById("archivesFilters.date").value)
        this.loadingArchives = true
        this.axios({
          url: "/api/superuser/tickets/archive/" + String(vm.newPageNumberArchives) + "/" + String(vm.rowsPerPageArchives),
          method: "GET",
          params: {
            id: vm.archivesFilters._id,
            from: vm.archivesFilters.from,
            date: vm.archivesFilters.date,
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            for (const i in res.data.data.ticketList) {
              res.data.data.ticketList[i].dateString = res.data.data.ticketList[i].creationDateTime.year + "/" + res.data.data.ticketList[i].creationDateTime.month + "/" + res.data.data.ticketList[i].creationDateTime.day
              res.data.data.ticketList[i].timeString = res.data.data.ticketList[i].creationDateTime.hours + ":" + res.data.data.ticketList[i].creationDateTime.minutes + ":" + res.data.data.ticketList[i].creationDateTime.seconds
            }
            vm.archives = res.data.data.ticketList
            vm.totalRecordsCountArchives = res.data.data.size
            vm.loadingArchives = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingArchives = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingArchives = false
        })
      } else if (command === "getTicket") {
        this.loadingArchives = true
        this.axios({
          url: "/api/user/ticket/" + vm.toolbarBuffer,
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.showTicketSupporter = res.data.data
            vm.showTicketSupporter.dateTimeString = res.data.data.creationDateTime.year + "/" + res.data.data.creationDateTime.month + "/" + res.data.data.creationDateTime.day + " - " +
            res.data.data.creationDateTime.hours + ":" + res.data.data.creationDateTime.minutes + ":" + res.data.data.creationDateTime.seconds
            vm.loadingArchives = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingArchives = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingArchives = false
        })
      }
    },
    userTicketsRequestMaster (command) {
      const vm = this
      let langCode = ""
      if (this.$i18n.locale === "Fa") {
        langCode = "fa"
      } else if (this.$i18n.locale === "En") {
        langCode = "en"
      }
      if (command === "getUserTickets") {
        this.loadingUserTickets = true
        this.axios({
          url: "/api/user/tickets/sent/" + String(vm.newPageNumberUserTickets) + "/" + String(vm.rowsPerPageUserTickets),
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            for (const i in res.data.data.ticketList) {
              res.data.data.ticketList[i].dateString = res.data.data.ticketList[i].creationDateTime.year + "/" + res.data.data.ticketList[i].creationDateTime.month + "/" + res.data.data.ticketList[i].creationDateTime.day
              res.data.data.ticketList[i].timeString = res.data.data.ticketList[i].creationDateTime.hours + ":" + res.data.data.ticketList[i].creationDateTime.minutes + ":" + res.data.data.ticketList[i].creationDateTime.seconds
            }
            vm.userTickets = res.data.data.ticketList
            vm.totalRecordsCountUserTickets = res.data.data.size
            vm.loadingUserTickets = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingUserTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingUserTickets = false
        })
      } else if (command === "getTicket") {
        this.loadingUserTickets = true
        this.axios({
          url: "/api/user/ticket/" + vm.toolbarBuffer,
          method: "GET",
          params: {
            lang: langCode
          }
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.replyTicketUser = res.data.data
            vm.replyTicketUser.dateTimeString = res.data.data.creationDateTime.year + "/" + res.data.data.creationDateTime.month + "/" + res.data.data.creationDateTime.day + " - " +
            res.data.data.creationDateTime.hours + ":" + res.data.data.creationDateTime.minutes + ":" + res.data.data.creationDateTime.seconds
            vm.loadingUserTickets = false
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingUserTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingUserTickets = false
        })
      } else if (command === "createTicket") {
        this.loadingUserTickets = true
        this.axios({
          url: "/api/user/ticket",
          method: "POST",
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            subject: vm.createSubjectUser,
            message: vm.createBodyUser
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingUserTickets = false
            vm.resetState("userTickets")
            vm.userTicketsRequestMaster("getUserTickets")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingUserTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingUserTickets = false
        })
      } else if (command === "replyTicket") {
        this.loadingUserTickets = true
        this.axios({
          url: "/api/user/ticket/reply/" + vm.replyTicketUser._id,
          method: "PUT",
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            message: vm.replyBodyUser
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingUserTickets = false
            vm.getTicket("userTickets", vm.replyTicketUser._id)
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingUserTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingUserTickets = false
        })
      } else if (command === "deleteTicket") {
        const toolbarBufferList = [this.toolbarBuffer]
        this.loadingUserTickets = true
        this.axios({
          url: "/api/user/tickets",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: toolbarBufferList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingUserTickets = false
            vm.userTicketsRequestMaster("getUserTickets")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingUserTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingUserTickets = false
        })
      } else if (command === "deleteTickets") {
        const toolbarBufferList = []
        for (const x in this.selectedTicketsUserTickets) {
          toolbarBufferList.push(this.selectedTicketsUserTickets[x]._id)
        }
        this.loadingUserTickets = true
        this.axios({
          url: "/api/user/tickets",
          method: "DELETE",
          headers: { "Content-Type": "application/json" },
          params: {
            lang: langCode
          },
          data: JSON.stringify({
            names: toolbarBufferList
          }).replace(/\\\\/g, "\\")
        }).then((res) => {
          if (res.data.status.code === 200) {
            vm.loadingUserTickets = false
            vm.userTicketsRequestMaster("getUserTickets")
          } else {
            vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
            vm.loadingUserTickets = false
          }
        }).catch(() => {
          vm.alertPromptMaster(vm.$t("requestError"), "", "pi-exclamation-triangle", "#FDB5BA")
          vm.loadingUserTickets = false
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
    getTicket (scale, id) {
      this.toolbarBuffer = id
      if (scale === "inbox") {
        this.supporterListFlag = false
        this.supporterReplyFlag = true
        this.inboxRequestMaster("getTicket")
      } else if (scale === "tickets") {
        this.supporterListFlag = false
        this.supporterReplyFlag = true
        this.ticketsRequestMaster("getTicket")
      } else if (scale === "archives") {
        this.supporterListFlag = false
        this.supporterShowFlag = true
        this.archivesRequestMaster("getTicket")
      } else if (scale === "userTickets") {
        this.userListFlag = false
        this.userReplyFlag = true
        this.userTicketsRequestMaster("getTicket")
      }
    },
    replyTicket (scale, action) {
      if (scale === "inbox") {
        if (action === "reply") {
          this.inboxRequestMaster("replyTicket")
        } else if (action === "reply&close") {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("close") + " " + this.$t("ticket"), "pi-question-circle", "#F0EAAA", this.inboxRequestMaster, "reply&closeTicket")
        }
      } else if (scale === "tickets") {
        if (action === "reply") {
          this.ticketsRequestMaster("replyTicket")
        } else if (action === "reply&close") {
          this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("close") + " " + this.$t("ticket"), "pi-question-circle", "#F0EAAA", this.ticketsRequestMaster, "reply&closeTicket")
        }
      } else if (scale === "userTickets") {
        if (action === "reply") {
          this.userTicketsRequestMaster("replyTicket")
        }
      }
    },
    deleteTicket (scale, id) {
      this.toolbarBuffer = id
      if (scale === "inbox") {
        this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + String(id), "pi-question-circle", "#F0EAAA", this.inboxRequestMaster, "deleteTicket")
      } else if (scale === "tickets") {
        this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + String(id), "pi-question-circle", "#F0EAAA", this.ticketsRequestMaster, "deleteTicket")
      } else if (scale === "userTickets") {
        this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + String(id), "pi-question-circle", "#F0EAAA", this.userTicketsRequestMaster, "deleteTicket")
      }
    },
    ticketingToolbar (scale, action) {
      if (scale === "inbox") {
        if (this.selectedTicketsInbox.length > 0) {
          if (action === "close") {
            this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("close") + " " + this.$t("tickets"), "pi-question-circle", "#F0EAAA", this.inboxRequestMaster, "closeTickets")
          } else if (action === "delete") {
            this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + this.$t("tickets"), "pi-question-circle", "#F0EAAA", this.inboxRequestMaster, "deleteTickets")
          }
        } else {
          this.alertPromptMaster(this.$t("noTicketSelected"), "", "pi-exclamation-triangle", "#FDB5BA")
        }
      } else if (scale === "tickets") {
        if (this.selectedTickets.length > 0) {
          if (action === "close") {
            this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("close") + " " + this.$t("tickets"), "pi-question-circle", "#F0EAAA", this.ticketsRequestMaster, "closeTickets")
          } else if (action === "delete") {
            this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + this.$t("tickets"), "pi-question-circle", "#F0EAAA", this.ticketsRequestMaster, "deleteTickets")
          }
        } else {
          this.alertPromptMaster(this.$t("noTicketSelected"), "", "pi-exclamation-triangle", "#FDB5BA")
        }
      } else if (scale === "userTickets") {
        if (action === "create") {
          this.userListFlag = false
          this.userCreateFlag = true
        } else if (action === "delete") {
          if (this.selectedTicketsUserTickets.length > 0) {
            this.confirmPromptMaster(this.$t("confirmPromptText"), this.$t("delete") + " " + this.$t("tickets"), "pi-question-circle", "#F0EAAA", this.userTicketsRequestMaster, "deleteTickets")
          } else {
            this.alertPromptMaster(this.$t("noTicketSelected"), "", "pi-exclamation-triangle", "#FDB5BA")
          }
        }
      }
    },
    removeFilters (scale, filter) {
      if (scale === "inbox") {
        if (filter === "_id") {
          this.inboxFilters._id = ""
        } else if (filter === "from") {
          this.inboxFilters.from = ""
        } else if (filter === "date") {
          document.getElementById("inboxFilters.date").value = ""
        } else if (filter === "all") {
          this.inboxFilters._id = ""
          this.inboxFilters.from = ""
          document.getElementById("inboxFilters.date").value = ""
        }
        this.inboxRequestMaster("getInbox")
      } else if (scale === "tickets") {
        if (filter === "_id") {
          this.ticketsFilters._id = ""
        } else if (filter === "from") {
          this.ticketsFilters.from = ""
        } else if (filter === "date") {
          document.getElementById("ticketsFilters.date").value = ""
        } else if (filter === "status") {
          this.ticketsFilters.status = {}
        } else if (filter === "all") {
          this.ticketsFilters._id = ""
          this.ticketsFilters.from = ""
          this.ticketsFilters.status = {}
          document.getElementById("ticketsFilters.date").value = ""
        }
        this.ticketsRequestMaster("getTickets")
      } else if (scale === "archives") {
        if (filter === "_id") {
          this.archivesFilters._id = ""
        } else if (filter === "from") {
          this.archivesFilters.from = ""
        } else if (filter === "date") {
          document.getElementById("archivesFilters.date").value = ""
        } else if (filter === "all") {
          this.archivesFilters._id = ""
          this.archivesFilters.from = ""
          document.getElementById("archivesFilters.date").value = ""
        }
        this.archivesRequestMaster("getArchives")
      }
    },
    resetState (command) {
      if (command === "inbox" || command === "tickets") {
        this.supporterReplyFlag = false
        this.supporterListFlag = true
        this.replyBodySupporter = ""
        this.replyTicketSupporter = {
          _id: "",
          from: "",
          subject: "",
          dateTimeString: "",
          messages: []
        }
      } else if (command === "archives") {
        this.supporterShowFlag = false
        this.supporterListFlag = true
        this.showTicketSupporter = {
          _id: "",
          from: "",
          subject: "",
          dateTimeString: "",
          messages: []
        }
      } else if (command === "userTickets") {
        this.userReplyFlag = false
        this.userCreateFlag = false
        this.userListFlag = true
        this.replyBodyUser = ""
        this.createSubjectUser = ""
        this.createBodyUser = ""
        this.replyTicketUser = {
          _id: "",
          from: "",
          subject: "",
          dateTimeString: "",
          messages: []
        }
      }
    },
    createTicket () {
      if (this.createSubjectUser === "") {
        this.alertPromptMaster(this.$t("invalidInputsError"), "", "pi-exclamation-triangle", "#FDB5BA")
      } else {
        this.userTicketsRequestMaster("createTicket")
      }
    },
    dateSerializer (date) {
      if (date !== "") {
        const tempArray = date.split(" ")
        return this.faNumToEnNum(tempArray[0]) + this.faMonthtoNumMonth(tempArray[1]) + this.faNumToEnNum(tempArray[2])
      } else {
        return ""
      }
    },
    faMonthtoNumMonth (faMonth) {
      let numMonth = ""
      switch (faMonth) {
        case "فروردین":
          numMonth = "01"
          break
        case "اردیبهشت":
          numMonth = "02"
          break
        case "خرداد":
          numMonth = "03"
          break
        case "تیر":
          numMonth = "04"
          break
        case "مرداد":
          numMonth = "05"
          break
        case "شهریور":
          numMonth = "06"
          break
        case "مهر":
          numMonth = "07"
          break
        case "آبان":
          numMonth = "08"
          break
        case "آذر":
          numMonth = "09"
          break
        case "دی":
          numMonth = "10"
          break
        case "بهمن":
          numMonth = "11"
          break
        case "اسفند":
          numMonth = "12"
          break
      }
      return numMonth
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
.p-column-filter-menu {
  margin: 0;
}
.sentMessage{
  border: 2px solid #dee2e6;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
  border-bottom-left-radius: 8px;
  float: right;
  background:  rgba(138, 250, 255, 0.5);
}
.receivedMessage{
  border: 2px solid #dee2e6;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
  border-bottom-right-radius: 8px;
  float: left;
  background: rgba(255, 248, 138, 0.5);
}
.alertMessage {
  border: 2px solid #dee2e6;
  border-radius: 8px;
  text-align: center;
  background: rgba(128, 140, 143, 0.5);
  float: right;
  right: 25%;
  position: relative;
}
</style>
