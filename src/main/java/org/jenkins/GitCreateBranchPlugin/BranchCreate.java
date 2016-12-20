package org.jenkins.GitCreateBranchPlugin;
import hudson.Launcher;
import hudson.Extension;
import hudson.model.Build;
import hudson.model.BuildListener;
import hudson.model.Descriptor.FormException;
import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.tasks.Builder;
import hudson.util.FormValidation;
import hudson.tasks.BuildStepDescriptor;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.DataBoundConstructor;
import org.kohsuke.stapler.QueryParameter;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.Descriptor;
import javax.servlet.ServletException;

import net.sf.json.JSONObject;

public class BranchCreate extends Builder {
   String repoPath ;
   String branchName;
   String authFileLocation;
  String commitMsg = "Creating a new branch in github";
  
  public String getRepoPath() {
	return repoPath;
}

public void setRepoPath(String repoPath) {
	this.repoPath = repoPath;
}

public String getBranchName() {
	return branchName;
}

public void setBranchName(String branchName) {
	this.branchName = branchName;
}

public String getAuthFileLocation() {
	return authFileLocation;
}

public void setAuthFileLocation(String authFileLocation) {
	this.authFileLocation = authFileLocation;
}


    // Fields in config.jelly must match the parameter names in the "DataBoundConstructor"
	@DataBoundConstructor
    public  BranchCreate(String repoPath, String branchName,String authFileLocation) {
		this.repoPath=repoPath;
		this.branchName=branchName;
		this.authFileLocation= authFileLocation;
		
	}

	@Override
	public boolean perform(AbstractBuild<?, ?> build,Launcher launcher, BuildListener listener) throws IOException, InterruptedException {
		this.repoPath=repoPath;
		this.branchName=branchName;
		this.authFileLocation=authFileLocation;
		boolean value = false;
		listener.getLogger().println(repoPath);
		listener.getLogger().println(branchName);
		listener.getLogger().println(authFileLocation);
		CommonUtilsGit git = new CommonUtilsGit(); 
		//String authCode = getAuthentication(authFileLocation);
		
		
		String patternToMatch = "[\\\\!\"#$%&()*+,./:;<=>?@\\[\\]^_{|}~]+";
		Pattern p = Pattern.compile(patternToMatch);
		Matcher m = p.matcher(branchName);
		boolean characterFound = m.find();
		
		if(characterFound==false)
		{
			
			value=git.createBranch(repoPath, branchName, authFileLocation, listener);
			listener.getLogger().println(value);		
	    	return value;
		}
		else
			listener.getLogger().println("Provide the branch name without special characters.");
			return false;
		
	}
		
	
		@Override
	    public DescriptorImpl getDescriptor() {
	        return (DescriptorImpl)super.getDescriptor();
	  }
		
    @Extension 
    public static final class DescriptorImpl extends BuildStepDescriptor<Builder> {
    	public DescriptorImpl() {
            load();
    	}
    	
    	
        public boolean isApplicable(Class<? extends AbstractProject> aClass) {
            // Indicates that this builder can be used with all kinds of project types 
            return true;
        }

        /**
         * This human readable name is used in the configuration screen.
         */
        public String getDisplayName() {
            return "Cloudset Create Branch";
        }

		
            
    }
	
}


