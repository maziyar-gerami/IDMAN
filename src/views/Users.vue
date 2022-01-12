<template>
  <div class="grid">
    <div class="col-12">
      <div class="card">
        <h5>{{ $t("users") }}</h5>
        <Toolbar>
          <template #start>
            <Button label="New" icon="pi pi-plus" class="p-button-success mx-1" />
            <Button label="Open" icon="pi pi-folder-open" class="p-button-secondary mx-1" />
            <i class="pi pi-bars p-toolbar-separator mx-1" ></i>
            <Button icon="pi pi-check" class="p-button-success mx-1" />
            <Button icon="pi pi-trash" class="p-button-warning mx-1" />
            <Button icon="pi pi-print" class="p-button-danger mx-1" />
          </template>

          <template #end>
            <Button icon="pi pi-check" class="p-button-success mx-1" />
            <Button icon="pi pi-trash" class="p-button-warning mx-1" />
            <Button icon="pi pi-print" class="p-button-danger mx-1" />
          </template>
        </Toolbar>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "Users"
}
</script>
