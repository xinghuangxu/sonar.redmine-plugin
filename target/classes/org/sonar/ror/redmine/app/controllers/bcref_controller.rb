class BcrefController < ApplicationController
    before_filter :init_resource_for_user_role, :load_properties

	SECTION=Navigation::SECTION_RESOURCE

	def showcharts
		@imgs={
			
			
			"Kingston"=>{
				"Kingston EIT"=>"Kingston/Kingston_Slide1.JPG",
				"Kingston EIT Stress Functional"=>"Kingston/Kingston_Slide2.JPG",
				"Kingston EIT Projection"=>"Kingston/Kingston_Slide3.JPG",
				"Kingston EIT Defects"=>"Kingston/Kingston_Slide4.JPG"
			},
			
			"Misbehaving"=>{
				"MBD"=>"MBD/MBD_Slide1.JPG",
				"MBD Iteration Charts"=>"MBD/MBD_Slide2.JPG",
				"MBD Planned Actual Resources"=>"MBD/MBD_Slide3.JPG",
				"MBD Defects"=>"MBD/MBD_Slide4.JPG"
			},
			
			"Rapid"=>{
				"RR"=>"RR/RR_Slide1.JPG",
				"RR Iteration Charts"=>"RR/RR_Slide2.JPG",
				"RR Planned Actual Resources"=>"RR/RR_Slide3.JPG",
				"RR Reconstruct Feature"=>"RR/RR_Slide4.JPG"
			},
			
			"Denali"=>{
				"Defect Trend"=>"Denali/Denali2_DefectTrend.GIF",
				"Iteration Burn Down"=>"Denali/Denali2_IterationBurnDown.GIF",
				"Iterations Velocity"=>"Denali/Denali2_IterationsVelocity.GIF",
				"Pln Act FTE Functional Group"=>"Denali/Denali2_PlnActFTE_FunctionalGroup.GIF",
				"Pln Act FTE"=>"Denali/Denali2_PlnActFTE.gif",
				"Pln Act Velocity"=>"Denali/Denali2_PlnActVelocity.GIF",
				"Story Burn Up"=>"Denali/Denali2_StoryBurnUp.GIF"
			}
			
		}
		
		
		@selectImgs=@imgs['Kingston']
		

		@imgs.each do |key,value|
		    if @resource.fullname.include? key
		        @selectImgs=value
		    end
		    if @refName.include? key
		    	@selectImgs=value
		    end
		end
	end
	
	
	private
	def load_properties
        @refEnable = Property.value(configuration::BCREF, @resource.id, "")
   	    @refName = Property.value(configuration::REF_NAME, @resource.id, "")
  	end
  	
	def configuration
         java_facade.getComponentByClassname('redmine', 'org.sonar.plugins.redmine.config.RedmineSettings').class
    end
end