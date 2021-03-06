declare var orcidGA: any;
declare var orcidVar: any;
declare var getBaseUri: any;

//Import all the angular components

import { NgForOf, NgIf } 
    from '@angular/common'; 

import { AfterViewInit, Component, OnDestroy, OnInit } 
    from '@angular/core';

import { Observable, Subject, Subscription } 
    from 'rxjs';
import { takeUntil } 
    from 'rxjs/operators';

import { ClientService } 
    from '../../shared/client.service.ts'; 

import { CommonService } 
    from '../../shared/common.service.ts'; 

@Component({
    selector: 'client-edit-ng2',
    template:  scriptTmpl("client-edit-ng2-template")
})
export class ClientEditComponent implements AfterViewInit, OnDestroy, OnInit {
    private ngUnsubscribe: Subject<void> = new Subject<void>();
    
    authorizeURL: any;
    authorizeUrlBase: any;
    authorizeURLTemplate: any;
    availableRedirectScopes: any;
    clients: any;
    clientDetails: any;
    clientToEdit: any;
    creating: any;
    editing: any;
    expanded: any;
    googleExampleLink: any;
    googleUri: any;
    hideGoogleUri: any;
    hideSwaggerMemberUri: any;
    hideSwaggerUri: any;
    listing: any;
    newClient: any;
    playgroundExample: any;
    resetThisClient: any;
    sampleAuthCurl: any;
    sampleAuthCurlTemplate: any;
    scopeSelectorOpen: any;
    selectedRedirectUri: any;
    selectedScope: any;
    selectedScopes: any;
    swaggerUri: any;
    swaggerMemberUri: any;
    tokenURL: any;
    viewing: any;
    selectedRedirectUriValue: any;

    constructor(
        private clientService: ClientService,
        private commonService: CommonService
    ) {
        this.authorizeURL = null;
        this.authorizeUrlBase = getBaseUri() + '/oauth/authorize';
        this.authorizeURLTemplate = this.authorizeUrlBase + '?client_id=[CLIENT_ID]&response_type=code&redirect_uri=[REDIRECT_URI]&scope=[SCOPES]';
        this.availableRedirectScopes = [];
        this.clients = [];
        this.clientDetails = {};
        this.clientToEdit = null;
        this.creating = false;
        this.editing = false;
        this.expanded = false;
        this.googleExampleLink = 'https://developers.google.com/oauthplayground/#step1&oauthEndpointSelect=Custom&oauthAuthEndpointValue=[BASE_URI_ENCODE]/oauth/authorize&oauthTokenEndpointValue=[BASE_URI_ENCODE]/oauth/token&oauthClientId=[CLIENT_ID]&oauthClientSecret=[CLIENT_SECRET]&accessTokenType=bearer&scope=[SCOPES]';
        this.googleUri = 'https://developers.google.com/oauthplayground';
        this.hideGoogleUri = true;
        this.hideSwaggerMemberUri = true;
        this.hideSwaggerUri = true;
        this.listing = true;
        this.newClient = null;
        this.playgroundExample = '';
        this.resetThisClient = null;
        this.sampleAuthCurl = '';
        this.sampleAuthCurlTemplate = "curl -i -L -k -H 'Accept: application/json' --data 'client_id=[CLIENT_ID]&client_secret=[CLIENT_SECRET]&grant_type=authorization_code&redirect_uri=[REDIRECT_URI]&code=REPLACE WITH OAUTH CODE' [BASE_URI]/oauth/token";
        this.scopeSelectorOpen = false;
        this.selectedRedirectUri = "";
        this.selectedScope = "";
        this.selectedScopes = [];
        this.swaggerUri = orcidVar.pubBaseUri+ '/v2.0/';
        this.swaggerMemberUri = this.swaggerUri.replace("pub","api");
        this.tokenURL = getBaseUri() + '/oauth/token';
        this.viewing = false;
        this.selectedRedirectUriValue = null;
    }

    addClient(): any {
        // Check which redirect uris are empty strings and remove them from the
        // array
        for(let j = this.newClient.redirectUris.length - 1; j >= 0 ; j--){
            if(!this.newClient.redirectUris[j].value){
                this.newClient.redirectUris.splice(j, 1);
            } else {
                this.newClient.redirectUris[j].actType.value = JSON.stringify({"import-works-wizard" : ["Articles"]});
                this.newClient.redirectUris[j].geoArea.value = JSON.stringify({"import-works-wizard" : ["Global"]});
            }
        }

        this.clientService.addClient( this.newClient )
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                if(data.errors != null && data.errors.length > 0){
                    this.newClient = data;
                } else {
                    // If everything worked fine, reload the list of clients
                    this.getClients();
                }
            },
            error => {
                //console.log('setformDataError', error);
            } 
        );
    };
    
    addTestRedirectUri (type, edit): void {
        var rUri = null;
        if(type == 'google'){
            rUri = this.googleUri;
            this.hideGoogleUri = true;
        } else if(type == 'swagger'){
            rUri = this.swaggerUri;
            this.hideSwaggerUri = true;
        } else if(type == 'swagger-member'){
            rUri = this.swaggerMemberUri;
            this.hideSwaggerMemberUri = true;
        }
        
        if(edit == 'true') {
            if(this.clientToEdit.redirectUris.length == 1 && this.clientToEdit.this[0].value.value == null) {
                this.clientToEdit.redirectUris[0].value.value = rUri;
            } else {
                 this.addUriToExistingClientTable(rUri);   
            }
        } else {
            if(this.newClient.redirectUris.length == 1 && this.newClient.redirectUris[0].value.value == null) {
                this.newClient.redirectUris[0].value.value = rUri;
            } else {
                 this.addRedirectUriToNewClientTable(rUri);   
            }
        }                    
    };

    // Add a new uri input field to a existing client
    addUriToExistingClientTable(defaultValue): void {
        defaultValue = (typeof defaultValue != undefined && defaultValue != null) ? defaultValue : '';     
        this.clientToEdit.redirectUris.push({value: {value: defaultValue},type: {value: 'default'}, scopes: [], errors: [], actType: {value: ""}, geoArea: {value: ""}});
    };
    
    // Add a new uri input field to a new client
    addRedirectUriToNewClientTable(defaultValue): void {
        defaultValue = (typeof defaultValue != undefined && defaultValue != null) ? defaultValue : '';                
        this.newClient.redirectUris.push({value: {value: defaultValue},type: {value: 'default'}, scopes: [], errors: [], actType: {value: ""}, geoArea: {value: ""}});
    };

    closeModal(): void{
        //$.colorbox.close();
    };

    collapse(): void{
        this.expanded = false;
    };

    confirmResetClientSecret(): void{
        this.resetThisClient = this.clientToEdit;
        /*
        $.colorbox({
            html : $compile($('#reset-client-secret-modal').html())($scope),
            transition: 'fade',
            onLoad: function() {
                $('#cboxClose').remove();
            },
            scrolling: true
        });
        */
        //$.colorbox.resize({width:"415px" , height:"250px"});
    };

    // Delete an uri input field
    deleteUriOnExistingClient(idx): void {
        this.clientToEdit.redirectUris.splice(idx, 1);
        this.hideGoogleUri = false;
        this.hideSwaggerUri = false;
        this.hideSwaggerMemberUri = false;
        if(this.clientToEdit.redirectUris != null && this.clientToEdit.redirectUris.length > 0) {
            for(let i = 0; i < this.clientToEdit.redirectUris.length; i++) {
                if(this.clientToEdit.redirectUris[i].value.value == this.googleUri) {
                    this.hideGoogleUri = true;
                }else if (this.swaggerUri == this.clientToEdit.redirectUris[i].value.value){
                    this.hideSwaggerUri = true;
                }else if (this.swaggerMemberUri == this.clientToEdit.redirectUris[i].value.value){
                    this.hideSwaggerMemberUri = true;
                }
            }
        }
    };

    // Delete an uri input field
    deleteUriOnNewClient(idx): void{
        this.newClient.redirectUris.splice(idx, 1);
        this.hideGoogleUri = false;
        this.hideSwaggerUri = false;
        this.hideSwaggerMemberUri = false;
        if(this.newClient.redirectUris != null && this.newClient.redirectUris.length > 0) {
            for(let i = 0; i < this.newClient.redirectUris.length; i++) {
                if(this.newClient.redirectUris[i].value.value == this.googleUri) {
                    this.hideGoogleUri = true;
                }else if (this.swaggerUri == this.newClient.redirectUris[i].value.value){
                    this.hideSwaggerUri = true;
                }else if (this.swaggerMemberUri == this.newClient.redirectUris[i].value.value){
                    this.hideSwaggerMemberUri = true;
                }
            }
        }
    };

    // Submits the updated client
    editClient(): void {
        // Check which redirect uris are empty strings and remove them from the
        // array
        for(let j = this.clientToEdit.redirectUris.length - 1; j >= 0 ; j--){
            if(!this.clientToEdit.redirectUris[j].value){
                this.clientToEdit.redirectUris.splice(j, 1);
            } else if(this.clientToEdit.redirectUris[j].actType.value == "") {
                this.clientToEdit.redirectUris[j].actType.value = JSON.stringify({"import-works-wizard" : ["Articles"]});
                this.clientToEdit.redirectUris[j].geoArea.value = JSON.stringify({"import-works-wizard" : ["Global"]});
            }
        }

        this.clientService.editClient( this.clientToEdit )
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                if(data.errors != null && data.errors.length > 0){
                    this.clientToEdit = data;
                } else {
                    // If everything worked fine, reload the list of clients
                    this.getClients();
                }
            },
            error => {
                //console.log('setformDataError', error);
            } 
        );

    };

    expand(): void{
        this.expanded = true;
    };

    getAvailableRedirectScopes(): any {
        var toRemove = '/authenticate';
        var result = [];

        result = jQuery.grep(this.availableRedirectScopes, function(value) {
          return value != toRemove;
        });

        return result;
    };

    // Get the list of clients associated with this user
    getClients(): void{

        this.clientService.getClients()
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                this.clients = data;
                this.creating = false;
                this.editing = false;
                this.viewing = false;
                this.listing = true;
                this.hideGoogleUri = false;
                this.hideSwaggerUri = false;
                this.hideSwaggerMemberUri = false;
            },
            error => {
                //console.log('getregisterDataError', error);
            } 
        );
    };

    getClientUrl(client): string {
        if(client != null) {
            if(client.website != null){
                if(client.website.value != null) {
                    if(client.website.value.lastIndexOf('http://') === -1 && client.website.value.lastIndexOf('https://') === -1) {
                        return '//' + client.website.value;
                    } else {
                        return client.website.value;
                    }
                }
            }
        }
        return '';
    };

    inputTextAreaSelectAll($event): void{
        $event.target.select();
    };

    // Checks if an item is selected
    isChecked(rUri): boolean {
        /*
        let scope = this.scope;
        if (jQuery.inArray( scope, rUri.scopes ) != -1) {
            return true;
        }
        */
        return false;
    };

    // Checks if the scope checkbox should be disabled
    isDisabled(rUri): boolean {
        if(rUri.type.value == 'grant-read-wizard') {
            return true;
        }
        return false;
    };

    // Load the list of scopes for client redirect uris
    loadAvailableScopes(): void {
        this.clientService.loadAvailableScopes()
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                this.availableRedirectScopes = data;
            },
            error => {
                //console.log('getregisterDataError', error);
            } 
        );
    };

    // Load the default scopes based n the redirect uri type selected
    loadDefaultScopes(rUri): void {
        // Empty the scopes to update the default ones
        rUri.scopes = [];
        // Fill the scopes with the default scopes
        if(rUri.type.value == 'grant-read-wizard'){
            rUri.scopes.push('/read-limited');
        } else if (rUri.type.value == 'import-works-wizard'){
            rUri.scopes.push('/read-limited');
            rUri.scopes.push('/activities/update');
        } else if (rUri.type.value == 'import-funding-wizard'){
            rUri.scopes.push('/read-limited');
            rUri.scopes.push('/activities/update');
        }
    };

    resetClientSecret(): void {
        this.clientService.resetClientSecret( this.resetThisClient.clientId.value )
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                if(data) {
                    this.editing = false;
                    this.creating = false;
                    this.listing = true;
                    this.viewing = false;

                    this.closeModal();
                    this.getClients();
                } else {
                    //console.log('Unable to reset client secret');
                }
            },
            error => {
                //console.log('setformDataError', error);
            } 
        );
    };

    // Mark an item as selected
    setSelectedItem(rUri): boolean {/*
        var scope = this.scope;
        if (jQuery.inArray( scope, rUri.scopes ) == -1) {
            rUri.scopes.push(scope);
        } else {
            rUri.scopes = jQuery.grep(rUri.scopes, function(value) {
                return value != scope;
              });
        }
        */
        return false;
    };

    // Get an empty modal to add
    showAddClient(): void {
        this.clientService.showAddClient()
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                this.newClient = data;
                this.creating = true;
                this.listing = false;
                this.editing = false;
                this.viewing = false;
                this.hideGoogleUri = false;
                this.hideSwaggerUri = false;
                this.hideSwaggerMemberUri = false;
            },
            error => {
                //console.log('getregisterDataError', error);
            } 
        );
    };

    // Display the modal to edit a client
    showEditClient(client): void {
        // Copy the client to edit to a scope variable
        this.clientToEdit = client;
        this.editing = true;
        this.creating = false;
        this.listing = false;
        this.viewing = false;
        this.hideGoogleUri = false;
        this.hideSwaggerUri = false;
        this.hideSwaggerMemberUri = false;

        if(this.clientToEdit.redirectUris != null && this.clientToEdit.redirectUris.length > 0) {
            for(var i = 0; i < this.clientToEdit.redirectUris.length; i++) {
                if(this.clientToEdit.redirectUris[i].value.value == this.googleUri) {
                    this.hideGoogleUri = true;
                }else if (this.swaggerUri == this.clientToEdit.redirectUris[i].value.value){
                    this.hideSwaggerUri = true;
                }else if (this.swaggerMemberUri == this.clientToEdit.redirectUris[i].value.value){
                    this.hideSwaggerMemberUri = true;
                }
            }
        }
    };

    showViewLayout(): void {
        this.editing = false;
        this.creating = false;
        this.listing = true;
        this.viewing = false;
    };

    // Submits the client update request
    submitEditClient(): void {
        // Check which redirect uris are empty strings and remove them from the
        // array
        for(var j = this.clientToEdit.length - 1; j >= 0 ; j--)    {
            if(!this.clientToEdit.redirectUris[j].value){
                this.clientToEdit.redirectUris.splice(j, 1);
            }
        }

        this.clientService.submitEditClient( this.clientToEdit )
        .pipe(    
            takeUntil(this.ngUnsubscribe)
        )
        .subscribe(
            data => {
                if(data.errors != null && data.errors.length > 0){
                    this.clientToEdit = data;
                } else {
                    // If everything worked fine, reload the list of clients
                    this.getClients();
                    this.closeModal();
                }
            },
            error => {
                //console.log('setformDataError', error);
            } 
        );
    };

    updateSelectedRedirectUri(): void {
        var clientId = '';
        var example = null;
        var sampleCurl = null;
        var scope = this.selectedScope;
        var selectedClientSecret = '';
        this.playgroundExample = '';

        if (this.clientDetails != null){
            clientId = this.clientDetails.clientId.value;
            selectedClientSecret = this.clientDetails.clientSecret.value;
        }

        if(this.selectedRedirectUri.length != 0) {
            this.selectedRedirectUriValue = this.selectedRedirectUri.value.value;

            if(this.googleUri == this.selectedRedirectUriValue) {
                example = this.googleExampleLink;
                example = example.replace('[BASE_URI_ENCODE]', encodeURI(getBaseUri()));
                example = example.replace('[CLIENT_ID]', clientId);
                example = example.replace('[CLIENT_SECRET]', selectedClientSecret);
                if(scope != '')
                    example = example.replace('[SCOPES]', scope);
                this.playgroundExample = example.replace(/,/g,'%20');
            }else if(this.swaggerUri == this.selectedRedirectUriValue) {
                this.playgroundExample = this.swaggerUri;
            }else if(this.swaggerMemberUri == this.selectedRedirectUriValue) {
                this.playgroundExample = this.swaggerMemberUri;
            }

            example = this.authorizeURLTemplate;
            example = example.replace('[BASE_URI]', orcidVar.baseUri);
            example = example.replace('[CLIENT_ID]', clientId);
            example = example.replace('[REDIRECT_URI]', this.selectedRedirectUriValue);
            if(scope != ''){
                example = example.replace('[SCOPES]', scope);
            }

            this.authorizeURL = example.replace(/,/g,'%20');    // replacing
                                                                    // ,

            // rebuild sample Auhtroization Curl
            sampleCurl = this.sampleAuthCurlTemplate;
            this.sampleAuthCurl = sampleCurl.replace('[CLIENT_ID]', clientId)
                .replace('[CLIENT_SECRET]', selectedClientSecret)
                .replace('[BASE_URI]', orcidVar.baseUri)
                .replace('[REDIRECT_URI]', this.selectedRedirectUriValue);
        }
    };

    // Display client details: Client ID and Client secret
    viewDetails(client): void {
        // Set the client details
        this.clientDetails = client;
        // Set the first redirect uri selected
        if(client.redirectUris != null && client.redirectUris.length > 0) {
            this.selectedRedirectUri = client.redirectUris[0];
        } else {
            this.selectedRedirectUri = null;
        }

        this.editing = false;
        this.creating = false;
        this.listing = false;
        this.viewing = true;

        // Update the selected redirect uri
        if(this.clientDetails != null){
            this.updateSelectedRedirectUri();
        }
    };

    //Default init functions provided by Angular Core
    ngAfterViewInit() {
        //Fire functions AFTER the view inited. Useful when DOM is required or access children directives
    };

    ngOnDestroy() {
        this.ngUnsubscribe.next();
        this.ngUnsubscribe.complete();
    };

    ngOnInit() {
        this.getClients();
        this.loadAvailableScopes();
    }; 
}