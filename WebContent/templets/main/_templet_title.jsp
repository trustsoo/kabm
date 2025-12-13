<%@page language="java" contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="/tld/menu" prefix="menu"%>
<%@ taglib uri="/tld/layout" prefix="layout"%>	

	<div class="pageTit">
		<h2><span class="tit_1">
			<menu:list depth="2">
				<menu:selected>
					<menu:isNotExistChild>
						<menu:param attr="name"/>
					</menu:isNotExistChild>
					<menu:list depth="3">
						<menu:selected>
							<menu:isNotExistChild>
								<menu:param attr="name"/>
							</menu:isNotExistChild>
							<menu:list depth="4">
									<menu:selected>
										<menu:param attr="name"/>
									</menu:selected>
							</menu:list>
						</menu:selected>
					</menu:list> 	
				</menu:selected>	
			</menu:list>
		</span></h2>
		
		<p class="rStep">
			<menu:list depth="2">
				<menu:selected>
					<span class=""><menu:param attr="name"/></span>
					<menu:list depth="3">
						<menu:selected>
							<menu:isExistChild>
								> <span class=""><menu:param attr="name"/></span>
							</menu:isExistChild>
							<menu:isNotExistChild>
								> <span class=""><menu:param attr="name"/></span>
							</menu:isNotExistChild>
							<menu:list depth="4">
								<menu:accessible>
									<menu:selected>
										> <span class=""><menu:param attr="name"/></span>
									</menu:selected>
								</menu:accessible>
							</menu:list>
						</menu:selected>
					</menu:list> 	
				</menu:selected>	
			</menu:list>
		</p>
	</div>
