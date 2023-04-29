<template>
  <div>
    <b-dropdown-item to="/operation">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.operation')">Operation</span>
    </b-dropdown-item>
    <b-dropdown-item to="/record">
      <font-awesome-icon icon="asterisk" />
      <span v-text="$t('global.menu.entities.record')">Record</span>
    </b-dropdown-item>
    <!-- jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here -->
  </div>
</template>

<script lang="ts" src="./entities-menu.component.ts"></script>
